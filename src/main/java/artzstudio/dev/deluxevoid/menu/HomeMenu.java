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

package artzstudio.dev.deluxevoid.menu;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.menu.home.WorldManager;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class HomeMenu {

    public static final int PAGE_SIZE = 45;
    private final DeluxeVoidWorld plugin;
    private final WorldManager worldManager;

    public HomeMenu(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.worldManager = new WorldManager(plugin);
    }

    public Inventory createInventory() {
        String title = addColor.setColors(plugin.getMenuHome().getString("MAIN.TITLE", "&8World Menu"));
        return Bukkit.createInventory(null, 54, title);
    }

    public void updateInventory(Inventory inv, Player player, int page) {
        List<String> allWorlds = worldManager.getSortedWorlds();

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allWorlds.size());

        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String worldKey = allWorlds.get(i);
            renderWorldItem(player, inv, slot, worldKey);
            slot++;
        }

        for (int i = slot; i < PAGE_SIZE; i++) {
            inv.setItem(i, null);
        }

        fillGlass(inv);
        setupStaticButtons(inv);
        setupNavigationButtons(inv, page, allWorlds.size(), endIndex);
    }

    private void renderWorldItem(Player player, Inventory inv, int slot, String key) {
        String matName = plugin.getWorlds().getString("WORLDS." + key + ".MATERIAL", "STONE");
        String spawnData = plugin.getWorlds().getString("WORLDS." + key + ".SPAWN");

        if (spawnData == null) return;
        String[] spawnParts = spawnData.split(",");
        String realWorldName = spawnParts[0];
        World world = Bukkit.getWorld(realWorldName);

        String playerCount = (world == null) ? "0" : UtilityFunctions.addCommas(world.getPlayers().size());

        String displayName = UtilityFunctions.setPlaceholders(
                plugin.getWorlds().getString("WORLDS." + key + ".WORLD") + " &7| &e( " + playerCount + " Player's )");

        List<String> lore = new ArrayList<>(plugin.getMenuHome().getStringList("MAIN.ALPHANUMERIC_ITEM.LORE"));
        lore.replaceAll(s -> processPlaceholders(s, player, key, spawnParts));

        UtilityFunctions.setItem(slot, inv, matName, displayName, lore);
    }

    private String processPlaceholders(String s, Player player, String key, String[] spawnParts) {
        boolean tpEnabled = "true".equals(plugin.getWorlds().getString("WORLDS." + key + ".TP-WHEN-FALLING"));
        s = UtilityFunctions.replacePlaceholder(s, "{Status}", plugin.getConfigYaml().getString(tpEnabled ? "ADMIN-CONFIG.PLACEHOLDER.ENABLED" : "ADMIN-CONFIG.PLACEHOLDER.DISABLED"));

        boolean isCurrent = player.getWorld().getName().equalsIgnoreCase(spawnParts[0]);
        s = UtilityFunctions.replacePlaceholder(s, "{Current World}", plugin.getFileTranslations().getString(isCurrent ? "MESSAGE_PLACEHOLDER_WORLD_YES" : "MESSAGE_PLACEHOLDER_WORLD_NO"));

        boolean alwaysDay = "true".equals(plugin.getWorlds().getString("WORLDS." + key + ".ALWAYS-DAY"));
        s = UtilityFunctions.replacePlaceholder(s, "{Status Day}", plugin.getConfigYaml().getString(alwaysDay ? "ADMIN-CONFIG.PLACEHOLDER.ENABLED" : "ADMIN-CONFIG.PLACEHOLDER.DISABLED"));

        s = UtilityFunctions.replacePlaceholder(s, "{Position}", plugin.getWorlds().getString("WORLDS." + key + ".VOID-POSITION"));
        s = UtilityFunctions.replacePlaceholder(s, "{X}", spawnParts[1]);
        s = UtilityFunctions.replacePlaceholder(s, "{Y}", spawnParts[2]);
        s = UtilityFunctions.replacePlaceholder(s, "{Z}", spawnParts[3]);
        s = UtilityFunctions.replacePlaceholder(s, "{Yaw}", spawnParts[4]);
        s = UtilityFunctions.replacePlaceholder(s, "{Pitch}", spawnParts[5]);

        return UtilityFunctions.setPlaceholders(s);
    }

    private void fillGlass(Inventory inv) {
        for (int i = 45; i <= 53; i++) UtilityFunctions.setGlass(i, inv);
    }

    private void setupStaticButtons(Inventory inv) {
        setItemFromConfig(inv, "MAIN.ADD_WORLD");
        setItemFromConfig(inv, "MAIN.CLOSE");
        setItemFromConfig(inv, "MAIN.RELOAD");
        setItemFromConfig(inv, "MAIN.CREATE_WORLD");
    }

    private void setItemFromConfig(Inventory inv, String path) {
        UtilityFunctions.setItem(
                plugin.getMenuHome().getInt(path + ".SLOT"), inv,
                plugin.getMenuHome().getString(path + ".MATERIAL", "BEDROCK"),
                plugin.getMenuHome().getString(path + ".NAME"),
                plugin.getMenuHome().getStringList(path + ".LORE")
        );
    }

    private void setupNavigationButtons(Inventory inv, int page, int totalItems, int endIndex) {
        int maxPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        if (maxPages == 0) maxPages = 1;

        int visualCurrentPage = page + 1;

        if (page > 0) {
            int slot = plugin.getMenuHome().getInt("MAIN.RETURN.SLOT");
            List<String> lore = new ArrayList<>();
            for (String s : plugin.getMenuHome().getStringList("MAIN.RETURN.LORE")) {
                lore.add(s.replace("{Current Page}", String.valueOf(visualCurrentPage))
                        .replace("{Max Pages}", String.valueOf(maxPages)));
            }
            UtilityFunctions.setItem(slot, inv, plugin.getMenuHome().getString("MAIN.RETURN.MATERIAL"), plugin.getMenuHome().getString("MAIN.RETURN.NAME"), lore);
        }

        if (endIndex < totalItems) {
            int slot = plugin.getMenuHome().getInt("MAIN.NEXT.SLOT");
            List<String> lore = new ArrayList<>();
            for (String s : plugin.getMenuHome().getStringList("MAIN.NEXT.LORE")) {
                lore.add(s.replace("{Current Page}", String.valueOf(visualCurrentPage))
                        .replace("{Max Pages}", String.valueOf(maxPages)));
            }
            UtilityFunctions.setItem(slot, inv, plugin.getMenuHome().getString("MAIN.NEXT.MATERIAL"), plugin.getMenuHome().getString("MAIN.NEXT.NAME"), lore);
        }
    }
}