package ruben_artz.world.launcher;

import developer.voidw.activate;
import developer.voidw.strings;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.commands.RegisterCommand;
import ruben_artz.world.configuration.UpdateConfig;
import ruben_artz.world.database.Cache;
import ruben_artz.world.events.chat.VOEditing;
import ruben_artz.world.events.inventory.VOInventoryClose;
import ruben_artz.world.events.inventory.click.VOInventoryClickCreate;
import ruben_artz.world.events.inventory.click.VOInventoryClickHome;
import ruben_artz.world.events.inventory.click.VOInventoryClickIcon;
import ruben_artz.world.events.inventory.click.VOInventoryClickPlayer;
import ruben_artz.world.events.updateEvent;
import ruben_artz.world.events.world.*;
import ruben_artz.world.firework.FireworkDamage;
import ruben_artz.world.firework.FireworkExplode;
import ruben_artz.world.utils.*;
import ruben_artz.world.utils.commands.PlayerCommand.MainCommand;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Launcher implements Launch {
    public static int numberWorlds;
    @Getter
    public static Cache cache;
    private static Launcher launcher;
    public DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public BukkitAudiences audiences;

    public static Launcher getInstance() {
        return launcher;
    }

    public static int getNumberWorlds() {
        return Bukkit.getWorlds().size();
    }

    @Override
    public void launch(DeluxeVoidWorld plugin) {
        launcher = this;

        audiences = BukkitAudiences.create(plugin);

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
        LoadWorld.loadWorld();
        LoadWorld.setTime();

        welcome();
    }

    @Override
    public void shutdown() {
        if (plugin.getGenerated().contains("WORLDS")) {
            for (String key : Objects.requireNonNull(plugin.getGenerated().getConfigurationSection("WORLDS")).getKeys(false)) {
                String[] name = Objects.requireNonNull(plugin.getGenerated().getString("WORLDS." + key + ".SPAWN")).split(",");
                UtilityFunctions.saveWorlds(name[0]);
                Bukkit.getLogger().log(Level.INFO, "[DeluxeVoidWorld] Saving world " + name[0] + "...");
            }
        }

        if (getCache() != null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> getCache().getMethod().shutdown());

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    plugin.getLogger().warning("Cache took too long to shut down. Skipping it.");
                }
            } catch (InterruptedException ignored) {
            }
        }

        NotificationManager.shutdown();
    }

    private void setRateLimit() {
        Audience audience = plugin.getAudiences();

        strings.setFalse(plugin.getConfig().getString("ADMIN-CONFIG.RATE_LIMIT"), audience);
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void registerEvents() {
        PluginManager event = plugin.getServer().getPluginManager();
        Arrays.asList(new updateEvent(),
                        new VOWorlds(),
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
        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
            new Slime();
        }

        NotificationManager.launch();
    }

    private void getCommands() {
        Objects.requireNonNull(plugin.getCommand("deluxevoidworld")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(plugin.getCommand("empty")).setExecutor(new MainCommand());
    }

    private void getMetrics() {
        UtilityFunctions.runTaskLater(60, () -> {
            final Metrics metrics = new Metrics(plugin, 9736);
            for (String key : Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false)) {
                if (plugin.getWorlds().getString("WORLDS." + key + ".TP-WHEN-FALLING") == null) {
                    metrics.addCustomChart(new SimplePie("active_teleports", () -> plugin.getWorlds().getString("WORLDS." + key + ".TP-WHEN-FALLING")));
                }
            }
            metrics.addCustomChart(new SimplePie("language", () -> plugin.getConfig().getString("ADMIN-CONFIG.LANGUAGE")));
            metrics.addCustomChart(new SingleLineChart("number_of_world_servers", () -> numberWorlds));
            metrics.addCustomChart(new SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
        });
    }

    private void setConnection() {
        cache = new Cache();
    }

    private void getNumbers() {
        UtilityFunctions.runTaskLater(15, () -> numberWorlds = Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false).size());
    }

    public void welcome() {
        UtilityFunctions.runTaskLater(16L, () -> {
            String linea1 = "___  ____ _    _  _ _  _ ____    _  _ ____ _ ___     _ _ _ ____ ____ _    ___  ";
            String linea2 = "|  \\ |___ |    |  |  \\/  |___    |  | |  | | |  \\    | | | |  | |__/ |    |  \\ ";
            String linea3 = "|__/ |___ |___ |__| _/\\_ |___     \\/  |__| | |__/    |_|_| |__| |  \\ |___ |__/ ";

            String color1 = "#54daf4";
            String color2 = "#545eb6";

            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea1 + "</gradient><reset>");
            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea2 + "</gradient><reset>");
            plugin.sendConsole("<gradient:" + color1 + ":" + color2 + ">" + linea3 + "</gradient><reset>");

            plugin.sendConsole("&f");
            plugin.sendConsole(plugin.getPrefix() + "&aSuccessfully enabled &cv" + plugin.version);
            plugin.sendConsole("&8--------------------------------------------------------------------------------------");
            plugin.sendConsole("&7         Developed by &c" + plugin.authors);
            plugin.sendConsole(plugin.getPrefix() + "&aVersion: &c" + plugin.version + " &ais loading... &8(&6Current&8)");
            plugin.sendConsole(plugin.getPrefix() + "&aServer: &c" + Bukkit.getVersion());
            plugin.sendConsole(plugin.getPrefix() + "&aLoading necessary files...");
            plugin.sendConsole("&f");
            plugin.sendConsole("&9[Loader] &fMaps loaded correctly: &f'&a" + Launcher.getNumberWorlds() + "&f'");
            plugin.sendConsole("&f");
            plugin.sendConsole("&fDeluxeVoidWorld &aStarting plugin...");
            plugin.sendConsole("&f");
            plugin.sendConsole("&8--------------------------------------------------------------------------------------");

            activate.setPolymart();
        });
    }
}