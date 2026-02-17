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

package artzstudio.dev.deluxevoid.menu.home;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.events.chat.playerInteractEvent;
import artzstudio.dev.deluxevoid.features.sendTitles;
import artzstudio.dev.deluxevoid.menu.HomeMenu;
import artzstudio.dev.deluxevoid.menu.icon.IconListener;
import artzstudio.dev.deluxevoid.session.SessionManager;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.WorldSettingsHandler;
import artzstudio.dev.deluxevoid.utils.addColor;
import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.*;

public class HomeListener implements Listener {

    private static final Map<UUID, Integer> pageSession = new HashMap<>();
    private final DeluxeVoidWorld plugin;
    private final HomeMenu homeMenu;
    private final WorldManager worldManager;
    private final Set<UUID> isSwitching = new HashSet<>();
    private final Map<UUID, MyScheduledTask> updateTasks = new HashMap<>();

    public HomeListener(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.homeMenu = new HomeMenu(plugin);
        this.worldManager = new WorldManager(plugin);
    }

    public void openInventory(Player player) {
        openPage(player, 0);
    }

    public void openRestore(Player player) {
        int page = pageSession.getOrDefault(player.getUniqueId(), 0);
        openPage(player, page);
    }

    private void openPage(Player player, int page) {
        MyScheduledTask oldTask = updateTasks.remove(player.getUniqueId());
        if (oldTask != null) oldTask.cancel();

        pageSession.put(player.getUniqueId(), page);

        Inventory inv = homeMenu.createInventory();
        homeMenu.updateInventory(inv, player, page);
        player.openInventory(inv);

        MyScheduledTask task = UtilityFunctions.runTaskTimer(20L, () -> {
            if (player.getOpenInventory().getTopInventory().equals(inv)) {
                homeMenu.updateInventory(inv, player, page);
            } else {
                MyScheduledTask t = updateTasks.remove(player.getUniqueId());
                if (t != null) t.cancel();
            }
        });
        updateTasks.put(player.getUniqueId(), task);
    }

    private void changePage(Player player, int page) {
        isSwitching.add(player.getUniqueId());
        openPage(player, page);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = addColor.setColors(plugin.getMenuHome().getString("MAIN.TITLE"));

        if (!ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(title))) return;
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        event.setCancelled(true);
        int slot = event.getSlot();
        int currentPage = pageSession.getOrDefault(player.getUniqueId(), 0);
        List<String> allWorlds = worldManager.getSortedWorlds();

        if (handleStaticButtons(player, slot, currentPage, allWorlds.size())) return;

        if (slot < HomeMenu.PAGE_SIZE) {
            int realIndex = (currentPage * HomeMenu.PAGE_SIZE) + slot;
            if (realIndex >= allWorlds.size()) return;

            String worldKey = allWorlds.get(realIndex);
            handleWorldAction(player, event.getClick(), worldKey, currentPage);
        }
    }

    private boolean handleStaticButtons(Player player, int slot, int currentPage, int totalItems) {
        if (slot == plugin.getMenuHome().getInt("MAIN.CLOSE.SLOT")) {
            player.closeInventory();
            return true;
        }

        if (slot == plugin.getMenuHome().getInt("MAIN.ADD_WORLD.SLOT")) {
            plugin.getSessionManager().setMode(player.getUniqueId(), SessionManager.SessionMode.ADD_WORLD_CHAT);
            player.closeInventory();

            playerInteractEvent.addTask(player.getUniqueId(), UtilityFunctions.runTaskTimer(50L, () -> {
                for (String Titlelist : plugin.getFileTranslations().getStringList("MESSAGE_CREATE_WORLD")) {
                    String[] Title = Titlelist.split(";");
                    UtilityFunctions.runTaskAsynchronously(() ->
                            sendTitles.sendTitle(player, Integer.parseInt(Title[0]), Integer.parseInt(Title[1]), Integer.parseInt(Title[2]), Title[3], Title[4]));
                }
            }));
            return true;
        }

        if (slot == plugin.getMenuHome().getInt("MAIN.CREATE_WORLD.SLOT")) {
            player.closeInventory();
            plugin.getSessionManager().setMode(player.getUniqueId(), SessionManager.SessionMode.CREATE_WORLD_CHAT);

            playerInteractEvent.addTask(player.getUniqueId(), UtilityFunctions.runTaskTimer(50L, () -> {
                for (String title : plugin.getFileTranslations().getStringList("MESSAGE_CREATE_WORLD_EMPTY")) {
                    String[] list = title.split(";");
                    UtilityFunctions.runTaskAsynchronously(() ->
                            sendTitles.sendTitle(player, Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]), list[3], list[4]));
                }
            }));
            return true;
        }

        if (slot == plugin.getMenuHome().getInt("MAIN.RELOAD.SLOT")) {
            Bukkit.dispatchCommand(player, "dew reload");
            return true;
        }

        int nextSlot = plugin.getMenuHome().getInt("MAIN.NEXT.SLOT");
        int prevSlot = plugin.getMenuHome().getInt("MAIN.RETURN.SLOT");

        if (slot == prevSlot && currentPage > 0) {
            UtilityFunctions.executeSound("UI_BUTTON_CLICK", player);
            changePage(player, currentPage - 1);
            return true;
        }
        if (slot == nextSlot && (currentPage + 1) * HomeMenu.PAGE_SIZE < totalItems) {
            UtilityFunctions.executeSound("UI_BUTTON_CLICK", player);
            changePage(player, currentPage + 1);
            return true;
        }
        return false;
    }

    private void handleWorldAction(Player player, ClickType click, String worldKey, int currentPage) {
        YamlDocument worlds = plugin.getWorlds();
        String path = "WORLDS." + worldKey;
        String spawnData = worlds.getString(path + ".SPAWN");
        if (spawnData == null) return;

        String realWorldName = spawnData.split(",")[0];
        World worldObj = Bukkit.getWorld(realWorldName);

        if (click == ClickType.LEFT) {
            worlds.remove(path);
            saveAndInitiate(worlds);

            String sound = plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.DELETED_WORLD_MENU");
            if (sound != null) UtilityFunctions.executeSound(sound, player);

            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_DELETE_WORLD"));
            openPage(player, 0);
        } else if (click == ClickType.RIGHT) {
            boolean current = worlds.getBoolean(path + ".TP-WHEN-FALLING", false);
            worlds.set(path + ".TP-WHEN-FALLING", !current);
            saveAndInitiate(worlds);

            String soundKey = !current ? "ADMIN-CONFIG.SOUNDS.ENABLED_VOID_TP" : "ADMIN-CONFIG.SOUNDS.DISABLED_VOID_TP";
            String sound = plugin.getConfigYaml().getString(soundKey);
            if (sound != null) UtilityFunctions.executeSound(sound, player);

            openPage(player, currentPage);
        } else if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT) {
            worldManager.setEditingWorld(player.getUniqueId(), worldKey);
            isSwitching.add(player.getUniqueId());
            new IconListener(plugin).openInventory(player);
        } else if (click == ClickType.NUMBER_KEY) {
            Bukkit.dispatchCommand(player, "dew teleport " + realWorldName);
        } else if (click == ClickType.MIDDLE) {
            if (player.getWorld().equals(worldObj)) {
                plugin.getSessionManager().setMode(player.getUniqueId(), SessionManager.SessionMode.EDIT_Y_CHAT);
                player.closeInventory();

                playerInteractEvent.addTask(player.getUniqueId(), UtilityFunctions.runTaskTimer(50L, () -> UtilityFunctions.runTaskAsynchronously(() -> {
                    for (String line : plugin.getFileTranslations().getStringList("MESSAGE_EDIT_BLOCK_Y")) {
                        String[] titleData = line.split(";");
                        sendTitles.sendTitle(player, Integer.parseInt(titleData[0]), Integer.parseInt(titleData[1]),
                                Integer.parseInt(titleData[2]), titleData[3], titleData[4]);
                    }
                })));
            } else {
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NOT_USE_ITEM"));
            }
        } else if (click == ClickType.CONTROL_DROP) {
            boolean day = worlds.getBoolean(path + ".ALWAYS-DAY", false);
            worlds.set(path + ".ALWAYS-DAY", !day);
            saveAndInitiate(worlds);
            openPage(player, currentPage);
        } else if (click == ClickType.DROP) {
            if (player.getWorld().equals(worldObj)) {
                Bukkit.dispatchCommand(player, "dew setworldvoid");
            } else {
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NOT_USE_ITEM"));
            }
        }
    }

    private void saveAndInitiate(YamlDocument doc) {
        try {
            doc.save();
            WorldSettingsHandler.updateAlwaysDayCache();
            plugin.initiate();
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = addColor.setColors(plugin.getMenuHome().getString("MAIN.TITLE"));
        if (ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(title))) {
            UUID uuid = event.getPlayer().getUniqueId();

            MyScheduledTask task = updateTasks.remove(uuid);
            if (task != null) task.cancel();

            if (isSwitching.contains(uuid)) {
                isSwitching.remove(uuid);
                return;
            }
            pageSession.remove(uuid);
        }
    }
}