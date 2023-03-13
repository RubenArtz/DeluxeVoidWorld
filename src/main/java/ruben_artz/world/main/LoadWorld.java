package ruben_artz.world.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import ruben_artz.world.world.VOManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadWorld {
    private final static VOMain plugin = VOMain.getPlugin(VOMain.class);

    public static void setTime() {
        VOManager.syncTaskLater(200, () -> VOManager.syncRunTaskTimer(200, () -> {
            for (String key : Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false)) {
                final String[] name = Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + key + ".SPAWN")).split(",");

                final World world = Bukkit.getWorld(name[0]);

                if (world == null) return;

                if (plugin.getWorlds().getBoolean("WORLDS." + key + ".ALWAYS-DAY")) {
                    world.setTime(8000);
                    world.setFullTime(8000);
                    world.setStorm(false);
                    world.setThundering(false);
                    world.setThunderDuration(2147483647);
                }
            }
        }));
    }

    public static void setWorlds() {
        VOManager.syncTaskLater(5, () -> {
            for (World worlds : Bukkit.getServer().getWorlds()) {
                final String worldName = worlds.getName();
                if (plugin.getWorlds().getString("WORLDS." + worldName + ".ALWAYS-DAY") == null) {
                    plugin.getWorlds().set("WORLDS." + worldName + ".ALWAYS-DAY", Boolean.TRUE);
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

    public static void ifConfigWorld() {
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

    public static void loadWorld() {
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
}
