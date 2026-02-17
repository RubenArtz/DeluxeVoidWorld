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

package artzstudio.dev.deluxevoid.events.inventory.click;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.data.world.WorldManager;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.utils.Slime;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class InventoryClickCreate implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void getCreates(InventoryClickEvent event) {
        YamlDocument config = plugin.getInventoryCrating();
        String title = addColor.setColors(config.getString("MAIN.TITLE", "Create World"));

        String expectedTitle = ChatColor.stripColor(title);
        String currentTitle = ChatColor.stripColor(event.getView().getTitle());

        if (!currentTitle.equals(expectedTitle)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        List<WorldManager> messages = plugin.getMessage();
        if (messages.isEmpty()) {
            player.closeInventory();
            return;
        }
        String worldName = messages.get(0).getName();

        if (slot == config.getInt("MAIN.NORMAL.SLOT")) {
            handleWorldCreation(player, worldName, "NORMAL");

        } else if (slot == config.getInt("MAIN.NETHER.SLOT")) {
            handleWorldCreation(player, worldName, "NETHER");

        } else if (slot == config.getInt("MAIN.THE_END.SLOT")) {
            handleWorldCreation(player, worldName, "THE_END");

        } else if (slot == config.getInt("MAIN.CLOSE.SLOT")) {
            player.closeInventory();

        } else if (slot == config.getInt("MAIN.RETURN.SLOT")) {
            Launcher.getInstance().getHomeListener().openInventory(player);
        }
    }

    private void handleWorldCreation(Player player, String worldName, String environment) {
        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
            Slime.createWorldWithSlime(player, worldName, environment);
        } else if (UtilityFunctions.isPluginEnabled("Multiverse-Core")) {
            UtilityFunctions.createWorldWithMultiverse(player, worldName, environment);
        } else if (UtilityFunctions.isPluginEnabled("UltraRegions")) {
            UtilityFunctions.createUltraWorld(player, worldName, environment);
        } else {
            UtilityFunctions.createWorldsDeluxe(player, worldName, environment);
        }

        player.closeInventory();
        plugin.removeMessages();
    }
}