package ruben_artz.world.launcher;

import developer.voidw.strings;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import ruben_artz.world.commands.RegisterCommand;
import ruben_artz.world.commands.VOPlayer;
import ruben_artz.world.configuration.UpdateConfig;
import ruben_artz.world.database.Cache;
import ruben_artz.world.events.chat.VOEditing;
import ruben_artz.world.events.inventory.VOInventoryClose;
import ruben_artz.world.events.inventory.click.VOInventoryClickCreate;
import ruben_artz.world.events.inventory.click.VOInventoryClickHome;
import ruben_artz.world.events.inventory.click.VOInventoryClickIcon;
import ruben_artz.world.events.inventory.click.VOInventoryClickPlayer;
import ruben_artz.world.events.world.*;
import ruben_artz.world.firework.FireworkDamage;
import ruben_artz.world.firework.FireworkExplode;
import ruben_artz.world.world.LoadWorld;
import ruben_artz.world.main.DeluxeVoidWorld;
import ruben_artz.world.world.VOManager;
import ruben_artz.world.world.VOSlime;
import ruben_artz.world.world.VOUpdater;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Launcher implements Launch {
    public DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    private static Launcher launcher;
    public static Launcher getInstance() {
        return launcher;
    }
    public static int numberWorlds;
    public BukkitAudiences audiences;
    @Getter public static Cache cache;

    @Override
    public void launch(DeluxeVoidWorld plugin) {
        launcher = this;
        plugin.LoadAllFiles();
        UpdateConfig.update();
        setRateLimit();
        getNumbers();
        getCommands();
        registerEvents();
        LoadWorld.setWorlds();
        LoadWorld.ifConfigWorld();
        getMetrics();
        setConnection();
        VOUpdater.setEnabled();
        LoadWorld.loadWorld();
        LoadWorld.setTime();
        plugin.getMessages();
    }

    @Override
    public void shutdown() {
        if (plugin.getGenerated().contains("WORLDS")) {
            for (String key : Objects.requireNonNull(plugin.getGenerated().getConfigurationSection("WORLDS")).getKeys(false)) {
                String[] name = Objects.requireNonNull(plugin.getGenerated().getString("WORLDS." + key + ".SPAWN")).split(",");
                VOManager.saveWorlds(name[0]);
                Bukkit.getLogger().log(Level.INFO, "[DeluxeVoidWorld] Saving world "+name[0]+"...");
            }
        }

        if(getCache() != null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> getCache().getMethod().shutdown());

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    plugin.getLogger().warning("Cache took too long to shut down. Skipping it.");
                }
            }catch(InterruptedException ignored){}
        }
    }

    private void setRateLimit() {
        strings.setFalse(plugin.getConfig().getString("ADMIN-CONFIG.RATE_LIMIT"));
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void registerEvents() {
        PluginManager event = plugin.getServer().getPluginManager();
        Arrays.asList(new VOWorlds(),
                        new VOEditing(),
                        new FireworkDamage(),
                        new FireworkExplode(),
                        new VOJoin(),
                        new VOInventoryClose(),
                        new VOLeave(),
                        new VODetect(),
                        new VOEntity(),
                        new VOInventoryClickHome(),
                        new VOInventoryClickCreate(),
                        new VOInventoryClickPlayer(),
                        new VOInventoryClickIcon())
                .forEach(listener -> event.registerEvents(listener, plugin));
        if (VOManager.isPluginEnabled("SlimeWorldManager")) {
            new VOSlime();
        }
        audiences = BukkitAudiences.create(plugin);
    }

    private void getCommands() {
        Objects.requireNonNull(plugin.getCommand("deluxevoidworld")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(plugin.getCommand("empty")).setExecutor(new VOPlayer());
    }

    private void getMetrics() {
        VOManager.syncTaskLater(60, () -> {
            final Metrics metrics = new Metrics(plugin, 9736);
            for (String key : Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false)) {
                if (plugin.getWorlds().getString("WORLDS."+key+".TP-WHEN-FALLING") == null) {
                    metrics.addCustomChart(new SimplePie("active_teleports", () -> plugin.getWorlds().getString("WORLDS."+key+".TP-WHEN-FALLING")));
                }
            }
            metrics.addCustomChart(new SimplePie("language", () -> plugin.getConfig().getString("ADMIN-CONFIG.LANGUAGE")));
            metrics.addCustomChart(new SingleLineChart("number_of_world_servers", () -> numberWorlds));
            metrics.addCustomChart(new SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
        });
    }

    private void  setConnection() {
        cache = new Cache();
    }

    private void getNumbers() {
        VOManager.syncTaskLater(15, () -> numberWorlds = Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false).size());
    }

    public static int getNumberWorlds() {
        return Bukkit.getWorlds().size();
    }
}