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

package artzstudio.dev.deluxevoid.events.chat;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.data.world.WorldManager;
import artzstudio.dev.deluxevoid.features.sendTitles;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.menu.Create;
import artzstudio.dev.deluxevoid.session.SessionManager;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class playerInteractEvent implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    private static final Map<UUID, MyScheduledTask> pendingTasks = new ConcurrentHashMap<>();

    public static void addTask(UUID uuid, MyScheduledTask task) {
        pendingTasks.put(uuid, task);
    }

    public static void removeTask(UUID uuid) {
        MyScheduledTask task = pendingTasks.remove(uuid);
        if (task != null) task.cancel();
    }

    @EventHandler
    public void getCancelEdition(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_AIR) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SessionManager.SessionMode mode = plugin.getSessionManager().getMode(uuid);

        if (mode == SessionManager.SessionMode.NONE) return;

        restorePlayerSession(player);
        plugin.getSessionManager().clearMode(uuid);

        if (mode == SessionManager.SessionMode.CREATE_WORLD_CHAT) {
            plugin.removeMessages();
        }

        removeTask(uuid);
        sendTitles.clearTitle(player);
    }

    @EventHandler
    public void createNewWorld(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getSessionManager().isInMode(player.getUniqueId(), SessionManager.SessionMode.CREATE_WORLD_CHAT)) {
            event.setCancelled(true);

            plugin.addMessage(new WorldManager(player.getName(), event.getMessage()));

            UtilityFunctions.runTask(() -> Create.openInventory(player));

            removeTask(player.getUniqueId());
            sendTitles.clearTitle(player);
        }
    }

    @EventHandler
    public void addWorldInventory(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getSessionManager().isInMode(player.getUniqueId(), SessionManager.SessionMode.ADD_WORLD_CHAT)) {
            event.setCancelled(true);

            YamlDocument worlds = getYamlDocument(event);
            try {
                worlds.save();
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }

            Runnable task = () -> {
                Launcher.getInstance().getHomeListener().openRestore(player);
                String sound = plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.CREATE_WORLD_MENU");
                if (sound != null) UtilityFunctions.executeSound(sound, player);
            };

            if (event.isAsynchronous()) UtilityFunctions.runTask(task);
            else task.run();

            plugin.getSessionManager().clearMode(player.getUniqueId());

            removeTask(player.getUniqueId());
            sendTitles.clearTitle(player);
        }
    }

    @EventHandler
    public void getEditBlockY(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getSessionManager().isInMode(player.getUniqueId(), SessionManager.SessionMode.EDIT_Y_CHAT)) {
            event.setCancelled(true);
            try {
                int number = Integer.parseInt(event.getMessage());
                if (number < 51) {
                    YamlDocument worlds = plugin.getWorlds();
                    worlds.set("WORLDS." + player.getWorld().getName() + ".VOID-POSITION", number);

                    try {
                        worlds.save();
                    } catch (IOException e) {
                        plugin.getLogger().severe(e.getMessage());
                    }

                    restorePlayerSession(player);

                    plugin.getSessionManager().clearMode(player.getUniqueId());

                    removeTask(player.getUniqueId());
                    sendTitles.clearTitle(player);
                } else {
                    playSound(player, "ADMIN-CONFIG.SOUNDS.CHANGE_Y_LESS_ZERO");
                    addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_LESS_ZERO"));
                }
            } catch (NumberFormatException e) {
                playSound(player, "ADMIN-CONFIG.SOUNDS.CHANGE_Y_NO_WORDS");
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NO_WORDS").replace("{Word}", event.getMessage()));
            }
        }
    }

    @EventHandler
    public void getChatLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getSessionManager().clearAll(uuid);

        removeTask(uuid);

        plugin.removeMessages();
    }

    private void restorePlayerSession(Player player) {
        Runnable openTask = () -> Launcher.getInstance().getHomeListener().openRestore(player);
        if (Bukkit.isPrimaryThread()) openTask.run();
        else UtilityFunctions.runTask(openTask);
    }

    private void playSound(Player player, String path) {
        String sound = plugin.getConfigYaml().getString(path);
        if (sound != null) UtilityFunctions.executeSound(sound, player);
    }

    private YamlDocument getYamlDocument(AsyncPlayerChatEvent event) {
        String worldName = event.getMessage();
        YamlDocument worlds = plugin.getWorlds();
        String path = "WORLDS." + worldName;

        worlds.set(path + ".ALWAYS-DAY", true);
        worlds.set(path + ".TP-WHEN-FALLING", true);
        worlds.set(path + ".VOID-POSITION", -5);
        worlds.set(path + ".WORLD", "&a" + worldName);
        worlds.set(path + ".MATERIAL", "STONE");
        worlds.set(path + ".SPAWN", UtilityFunctions.setLocation(worldName));
        worlds.set(path + ".COMMANDS.TYPE", "CONSOLE");

        List<String> listCommands = worlds.getStringList(path + ".COMMANDS.LIST");
        listCommands.add("tell {Player} &cThe lobby is not over there!");
        worlds.set(path + ".COMMANDS.LIST", listCommands);
        return worlds;
    }
}