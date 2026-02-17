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

package artzstudio.dev.deluxevoid.events.world;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.WorldSettingsHandler;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.List;

public class WorldInitEvent implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onWorldCreate(org.bukkit.event.world.WorldInitEvent event) {
        String worldName = event.getWorld().getName();
        YamlDocument config = plugin.getConfigYaml();
        YamlDocument worlds = plugin.getWorlds();

        if (config.getBoolean("ADMIN-CONFIG.AUTOMATICALLY-DETECT-NEW-WORLDS", false)) {
            String path = "WORLDS." + worldName;

            if (!worlds.contains(path)) {
                worlds.set(path + ".ALWAYS-DAY", true);
                worlds.set(path + ".TP-WHEN-FALLING", true);
                worlds.set(path + ".VOID-POSITION", -1);
                worlds.set(path + ".WORLD", "&a" + worldName);
                worlds.set(path + ".MATERIAL", "STONE");
                worlds.set(path + ".SPAWN", UtilityFunctions.setLocation(event.getWorld().getSpawnLocation()));
                worlds.set(path + ".COMMANDS.TYPE", "CONSOLE");

                List<String> listCommands = worlds.getStringList(path + ".COMMANDS.LIST");
                if (listCommands.isEmpty()) {
                    listCommands.add("tell {Player} &cThe lobby is not over there!");
                }
                worlds.set(path + ".COMMANDS.LIST", listCommands);

                try {
                    worlds.save();
                    WorldSettingsHandler.updateAlwaysDayCache();
                } catch (IOException e) {
                    plugin.getLogger().severe("Could not save worlds.yml: " + e.getMessage());
                }

                String notifyMessage = plugin.getFileTranslations().getString("MESSAGE_NEW_MAP");
                if (notifyMessage != null) {
                    Bukkit.getOnlinePlayers().stream()
                            .filter(player -> player.isOp() || player.hasPermission("DeluxeVoidWorld.Admin"))
                            .forEach(player -> addColor.sendMessage(player, notifyMessage));
                }
            }
        }
    }
}