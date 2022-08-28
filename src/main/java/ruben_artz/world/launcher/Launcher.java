package ruben_artz.world.launcher;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import ruben_artz.world.commands.RegisterCommand;
import ruben_artz.world.commands.VOPlayer;
import ruben_artz.world.database.MySQL;
import ruben_artz.world.database.SQLite;
import ruben_artz.world.events.chat.VOEditing;
import ruben_artz.world.events.inventory.VOInventoryClose;
import ruben_artz.world.events.inventory.click.VOInventoryClickCreate;
import ruben_artz.world.events.inventory.click.VOInventoryClickHome;
import ruben_artz.world.events.inventory.click.VOInventoryClickIcon;
import ruben_artz.world.events.inventory.click.VOInventoryClickPlayer;
import ruben_artz.world.events.world.*;
import ruben_artz.world.firework.FireworkDamage;
import ruben_artz.world.firework.FireworkExplode;
import ruben_artz.world.main.VOConfig;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;
import ruben_artz.world.world.VOSlime;
import ruben_artz.world.world.VOUpdater;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Launcher implements Launch {
    public VOMain plugin = VOMain.getPlugin(VOMain.class);
    private static Launcher launcher;
    public static Launcher getInstance() {
        return launcher;
    }
    public static int numberWorlds;
    public BukkitAudiences audiences;

    @Override
    public void launch(VOMain plugin) {
        launcher = this;
        plugin.LoadAllFiles();
        VOConfig.update();
        getNumbers();
        getCommands();
        getEvents();
        setWorlds();
        ifConfigWorld();
        getMetrics();
        setConnection();
        VOUpdater.setEnabled();
        loadWorld();
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
        if (VOManager.isMySQL()) {
            MySQL.shutdown();
        } else {
            SQLite.shutdown();
        }
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void getEvents() {
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

    private void setWorlds() {
        VOManager.syncTaskLater(5, () -> {
            for (World worlds : Bukkit.getServer().getWorlds()) {
                final String worldName = worlds.getName();
                if (plugin.getWorlds().getString("WORLDS." + worldName + ".DEFAULT-WORLD") == null) {
                    plugin.getWorlds().set("WORLDS." + worldName + ".DEFAULT-WORLD", Boolean.FALSE);
                    plugin.getWorlds().set("WORLDS." + worldName + ".TP-WHEN-FALLING", Boolean.TRUE);
                    plugin.getWorlds().set("WORLDS." + worldName + ".VOID-POSITION", -1);
                    plugin.getWorlds().set("WORLDS." + worldName + ".WORLD", "&a" + worldName + "");
                    plugin.getWorlds().set("WORLDS." + worldName + ".MATERIAL", "STONE");
                    plugin.getWorlds().set("WORLDS." + worldName + ".SPAWN", VOManager.setLocation(worlds.getSpawnLocation()));
                    plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.TYPE", "CONSOLE");
                    final List<String> listCommands = plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST");
                    listCommands.add("tell {Player} &cThe lobby is not over there!");
                    plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.LIST", listCommands);
                    plugin.files.saveFile("worlds.yml");
                }
            }
            if (plugin.getConfig().contains("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                for (String keys : plugin.getConfig().getStringList("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                    final String[] name = keys.split(",");
                    if (plugin.getGenerated().getString(("WORLDS." + name[0] + ".ENVIROMENT")) == null) {
                        plugin.getGenerated().set("WORLDS."+name[0]+".ENVIROMENT", name[6]);
                        plugin.getGenerated().set("WORLDS."+name[0]+".WORLD-TYPE", "FLAT");
                        plugin.getGenerated().set("WORLDS."+name[0]+".DIFFICULTY", "NORMAL");
                        plugin.getGenerated().set("WORLDS."+name[0]+".SPAWN-FLAGS", true);
                        plugin.getGenerated().set("WORLDS."+name[0]+".PVP", true);
                        plugin.getGenerated().set("WORLDS."+name[0]+".STORM", false);
                        plugin.getGenerated().set("WORLDS."+name[0]+".THUNDERING", false);
                        plugin.getGenerated().set("WORLDS."+name[0]+".WEATHER-DURATION", 2147483647);
                        plugin.getGenerated().set("WORLDS."+name[0]+ ".BORDER-SIZE", 300);
                        plugin.getGenerated().set("WORLDS."+name[0]+".AUTO-SAVE", false);
                        plugin.getGenerated().set("WORLDS."+name[0]+".SPAWN", VOManager.setLocation(name[0]));
                        plugin.files.saveFile("generated.yml");
                    }
                }
            }
        });
    }

    private void ifConfigWorld() {
        VOManager.syncTaskLater(5, () -> {
            for (String keys : plugin.getConfig().getStringList("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                String[] name = keys.split(",");
                if (plugin.getGenerated().getString(("WORLDS." + name[0] + ".ENVIROMENT")) == null) {
                    if (plugin.getConfig().contains("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                        plugin.getGenerated().set("WORLDS." + name[0] + ".ENVIROMENT", name[6]);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".WORLD-TYPE", "FLAT");
                        plugin.getGenerated().set("WORLDS." + name[0] + ".DIFFICULTY", "NORMAL");
                        plugin.getGenerated().set("WORLDS." + name[0] + ".SPAWN-FLAGS", true);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".PVP", true);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".STORM", false);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".THUNDERING", false);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".WEATHER-DURATION", 2147483647);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".BORDER-SIZE", 300);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".AUTO-SAVE", false);
                        ArrayList<String> gamerules = (ArrayList<String>) plugin.getGenerated().getStringList("WORLDS." + name[0] + ".GAME-RULES");
                        gamerules.add("doDaylightCycle:false");
                        gamerules.add("mobGriefing:false");
                        gamerules.add("doFireTick:false");
                        gamerules.add("showDeathMessages:false");
                        gamerules.add("randomTickSpeed:false");
                        plugin.getGenerated().set("WORLDS." + name[0] + ".GAME-RULES", gamerules);
                        plugin.getGenerated().set("WORLDS." + name[0] + ".SPAWN", VOManager.setLocation(name[0]));
                        plugin.files.saveFile("generated.yml");
                    }
                }
            }
        });
    }

    private void loadWorld() {
        VOManager.syncTaskLater(10, () -> {
            if (plugin.getGenerated().contains("WORLDS")) {
                for (String key : Objects.requireNonNull(plugin.getGenerated().getConfigurationSection("WORLDS")).getKeys(false)) {
                    String source = Bukkit.getWorldContainer().getPath();
                    File src = new File(source);
                    for (File file : Objects.requireNonNull(src.listFiles())) {
                        if (file.isDirectory()) {
                            if (file.getName().equals("cache") || file.getName().equals("plugins") || file.getName().equals("logs")) {
                                String[] name = Objects.requireNonNull(plugin.getGenerated().getString("WORLDS." + key + ".SPAWN")).split(",");
                                VOManager.createEmptyWorld(name[0],
                                        plugin.getGenerated().getString("WORLDS." + key + ".ENVIROMENT", "NORMAL"),
                                        plugin.getGenerated().getString("WORLDS." + key + ".WORLD-TYPE", "FLAT"),
                                        plugin.getGenerated().getString("WORLDS." + key + ".DIFFICULTY", "PEACEFUL"),
                                        plugin.getGenerated().getBoolean("WORLDS." + key + ".SPAWN-FLAGS", true),
                                        plugin.getGenerated().getBoolean("WORLDS." + key + ".PVP", true),
                                        plugin.getGenerated().getBoolean("WORLDS." + key + ".STORM", false),
                                        plugin.getGenerated().getBoolean("WORLDS." + key + ".THUNDERING", false),
                                        plugin.getGenerated().getInt("WORLDS." + key + ".WEATHER-DURATION", 2147483647),
                                        plugin.getGenerated().getBoolean("WORLDS." + key + ".AUTO-SAVE", true),
                                        plugin.getGenerated().getInt("WORLDS."+key+".BORDER-SIZE", 100));
                            }
                        }
                    }
                }
            }
        });
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
        if (VOManager.isMySQL()) {
            MySQL mySQL = new MySQL();
            mySQL.init(plugin);
        } else {
            SQLite sqlite = new SQLite();
            sqlite.init();
        }
    }

    private void getNumbers() {
        VOManager.syncTaskLater(15, () -> numberWorlds = Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false).size());
    }

    public static int getNumberWorlds() {
        return Bukkit.getWorlds().size();
    }
}