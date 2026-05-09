/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.utils;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldSettingsHandler {
    private final static DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    private static final Map<World, Boolean> alwaysDayCache = new HashMap<>();

    public static void updateAlwaysDayCache() {
        alwaysDayCache.clear();
        Section worldsSection = plugin.getWorlds().getSection("WORLDS");

        for (Object key : worldsSection.getKeys()) {
            String worldKey = key.toString();
            String spawnData = worldsSection.getString(worldKey + ".SPAWN");

            if (spawnData == null) continue;

            String worldName = spawnData.split(",")[0];
            World world = Bukkit.getWorld(worldName);

            if (world != null && worldsSection.getBoolean(worldKey + ".ALWAYS-DAY", false)) {
                alwaysDayCache.put(world, true);
            }
        }
    }

    public static void setTime() {
        UtilityFunctions.runTaskLater(100L, () -> {
            updateAlwaysDayCache();

            UtilityFunctions.runTaskTimer(20L, () -> {
                if (alwaysDayCache.isEmpty()) return;

                java.util.Iterator<Map.Entry<World, Boolean>> iterator = alwaysDayCache.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<World, Boolean> entry = iterator.next();
                    World world = entry.getKey();

                    if (world == null) {
                        iterator.remove();
                        continue;
                    }

                    try {
                        if (world.getEnvironment() != World.Environment.CUSTOM) {
                            Boolean daylightCycle = world.getGameRuleValue(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE);

                            if (daylightCycle != null && daylightCycle) {
                                if (world.getTime() != 8000L) {
                                    world.setTime(8000L);
                                }
                            } else {
                                try {
                                    if (world.getFullTime() % 24000 != 8000L) {
                                        long baseTime = (world.getFullTime() / 24000) * 24000;
                                        world.setFullTime(baseTime + 8000L);
                                    }
                                } catch (IllegalArgumentException e) {
                                    if (e.getMessage().contains("world clock")) {
                                        iterator.remove();
                                        plugin.getLogger().info("Removed world '" + world.getName() + "' from always-day cache (no world clock)");
                                    } else {
                                        throw e;
                                    }
                                }
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().contains("world clock")) {
                            iterator.remove();
                        } else {
                            plugin.getLogger().warning("Error setting time for world: " + world.getName() + " - " + e.getMessage());
                        }
                    }

                    try {
                        if (world.hasStorm()) {
                            world.setStorm(false);
                            world.setThundering(false);
                        }
                    } catch (Exception ignored) {
                    }
                }
            });
        });
    }

    public static void setWorlds() {
        UtilityFunctions.runTaskLater(5, () -> {
            YamlDocument worldsCfg = plugin.getWorlds();

            for (World world : Bukkit.getServer().getWorlds()) {
                String worldName = world.getName();
                String path = "WORLDS." + worldName;

                if (!worldsCfg.contains(path + ".ALWAYS-DAY")) {
                    worldsCfg.set(path + ".ALWAYS-DAY", true);
                    worldsCfg.set(path + ".TP-WHEN-FALLING", true);
                    worldsCfg.set(path + ".VOID-POSITION", -1);
                    worldsCfg.set(path + ".WORLD", "&a" + worldName);
                    worldsCfg.set(path + ".MATERIAL", "STONE");
                    worldsCfg.set(path + ".SPAWN", UtilityFunctions.setLocation(world.getSpawnLocation()));
                    worldsCfg.set(path + ".COMMANDS.TYPE", "CONSOLE");

                    List<String> listCommands = worldsCfg.getStringList(path + ".COMMANDS.LIST");
                    listCommands.add("tell {Player} &cThe lobby is not over there!");
                    worldsCfg.set(path + ".COMMANDS.LIST", listCommands);

                    try {
                        worldsCfg.save();
                    } catch (IOException e) {
                        plugin.getLogger().severe(e.getMessage());
                    }
                }
            }

            YamlDocument mainConfig = plugin.getConfigYaml();
            if (mainConfig.contains("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                YamlDocument generated = plugin.getGenerated();

                for (String keys : mainConfig.getStringList("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                    String[] name = keys.split(",");
                    String worldName = name[0];
                    String path = "WORLDS." + worldName;

                    if (!generated.contains(path + ".ENVIROMENT")) {
                        generated.set(path + ".ENVIROMENT", name.length > 6 ? name[6] : "NORMAL");
                        generated.set(path + ".WORLD-TYPE", "FLAT");
                        generated.set(path + ".DIFFICULTY", "NORMAL");
                        generated.set(path + ".SPAWN-FLAGS", true);
                        generated.set(path + ".PVP", true);
                        generated.set(path + ".STORM", false);
                        generated.set(path + ".THUNDERING", false);
                        generated.set(path + ".WEATHER-DURATION", Integer.MAX_VALUE);
                        generated.set(path + ".BORDER-SIZE", 300);
                        generated.set(path + ".AUTO-SAVE", false);
                        generated.set(path + ".SPAWN", UtilityFunctions.setLocation(worldName));

                        try {
                            generated.save();
                        } catch (IOException e) {
                            plugin.getLogger().severe(e.getMessage());
                        }
                    }
                }
            }
        });
    }

    public static void ifConfigWorld() {
        UtilityFunctions.runTaskLater(5, () -> {
            YamlDocument config = plugin.getConfigYaml();
            YamlDocument generated = plugin.getGenerated();

            for (String keys : config.getStringList("ON_VOID_TP.DO_NOT_TOUCH_WORLDS")) {
                String[] name = keys.split(",");
                String worldName = name[0];
                String path = "WORLDS." + worldName;

                if (!generated.contains(path + ".ENVIROMENT")) {
                    generated.set(path + ".ENVIROMENT", name.length > 6 ? name[6] : "NORMAL");
                    generated.set(path + ".WORLD-TYPE", "FLAT");
                    generated.set(path + ".DIFFICULTY", "NORMAL");
                    generated.set(path + ".SPAWN-FLAGS", true);
                    generated.set(path + ".PVP", true);
                    generated.set(path + ".STORM", false);
                    generated.set(path + ".THUNDERING", false);
                    generated.set(path + ".WEATHER-DURATION", 2147483647);
                    generated.set(path + ".BORDER-SIZE", 300);
                    generated.set(path + ".AUTO-SAVE", false);

                    List<String> gamerules = generated.getStringList(path + ".GAME-RULES");
                    gamerules.add("doDaylightCycle:false");
                    gamerules.add("mobGriefing:false");
                    gamerules.add("doFireTick:false");
                    gamerules.add("showDeathMessages:false");
                    gamerules.add("randomTickSpeed:false");
                    generated.set(path + ".GAME-RULES", gamerules);

                    generated.set(path + ".SPAWN", UtilityFunctions.setLocation(worldName));

                    try {
                        generated.save();
                    } catch (IOException e) {
                        plugin.getLogger().severe(e.getMessage());
                    }
                }
            }
        });
    }

    public static void loadWorld() {
        UtilityFunctions.runTaskLater(10, () -> {
            YamlDocument gen = plugin.getGenerated();
            dev.dejvokep.boostedyaml.block.implementation.Section worldsSection = gen.getSection("WORLDS");

            if (worldsSection != null) {
                for (Object key : worldsSection.getKeys()) {
                    String keyStr = key.toString();

                    String source = Bukkit.getWorldContainer().getPath();
                    File src = new File(source);
                    File[] files = src.listFiles();

                    if (files == null) continue;

                    for (File file : files) {
                        if (file.isDirectory()) {
                            String folderName = file.getName();
                            if (folderName.equals("cache") || folderName.equals("plugins") || folderName.equals("logs")) {

                                String spawnData = worldsSection.getString(keyStr + ".SPAWN");
                                if (spawnData == null) continue;

                                String[] name = spawnData.split(",");

                                UtilityFunctions.createEmptyWorld(
                                        name[0],
                                        worldsSection.getString(keyStr + ".ENVIROMENT", "NORMAL"),
                                        worldsSection.getString(keyStr + ".WORLD-TYPE", "FLAT"),
                                        worldsSection.getString(keyStr + ".DIFFICULTY", "PEACEFUL"),
                                        worldsSection.getBoolean(keyStr + ".SPAWN-FLAGS", true),
                                        worldsSection.getBoolean(keyStr + ".PVP", true),
                                        worldsSection.getBoolean(keyStr + ".STORM", false),
                                        worldsSection.getBoolean(keyStr + ".THUNDERING", false),
                                        worldsSection.getInt(keyStr + ".WEATHER-DURATION", 2147483647),
                                        worldsSection.getBoolean(keyStr + ".AUTO-SAVE", true),
                                        worldsSection.getInt(keyStr + ".BORDER-SIZE", 100)
                                );
                            }
                        }
                    }
                }
            }
        });
    }
}