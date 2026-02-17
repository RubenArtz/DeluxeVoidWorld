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
import artzstudio.dev.deluxevoid.data.player.PlayerData;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class playerMoveEvent implements Listener {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    private final Set<UUID> processing = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoveTeleportation(PlayerMoveEvent event) {
        if (event.getFrom().getBlockY() == event.getTo().getBlockY()) return;

        final Player player = event.getPlayer();
        if (processing.contains(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
        final Section worldSection = plugin.getWorlds().getSection("WORLDS." + worldName);

        if (worldSection == null) return;

        int voidPos = worldSection.getInt("VOID-POSITION", 0);
        int currentY = event.getTo().getBlockY();

        if (currentY <= voidPos + 15 && currentY > voidPos) {
            setEffect("ADD", player);
            return;
        }

        if (currentY <= voidPos && worldSection.getBoolean("TP-WHEN-FALLING", false)) {
            processing.add(player.getUniqueId());

            setEffect("REMOVE", player);
            handleVoidActions(player, worldSection);

            UtilityFunctions.runTaskLater(40L, () -> processing.remove(player.getUniqueId()));
        }
    }

    private void handleVoidActions(Player player, Section worldSection) {
        PlayerData data = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (data.isTeleportEnabled()) {
            UtilityFunctions.getTeleportation(player);
        }

        if (data.isJumpEnabled()) {
            plugin.getSessionManager().addDamageImmunity(player.getUniqueId());
            UtilityFunctions.getJumpEffects(player);
        }

        if (data.isLightningEnabled()) {
            UtilityFunctions.getLightningEffect(player);
        }

        if (data.isParticlesEnabled()) {
            UtilityFunctions.getParticles(player);
        }

        sendCommands(player, worldSection);
        UtilityFunctions.isNull(player);
    }

    private void setEffect(String type, Player player) {
        if (plugin.getConfigYaml().getBoolean("ON_VOID_TP.SETTINGS.BACKGROUND_EFFECT.ENABLED", false)) {
            UtilityFunctions.sendBackGround(type, player);
        }
    }

    private void sendCommands(Player player, Section worldSection) {
        String type = worldSection.getString("COMMANDS.TYPE", "CONSOLE");
        List<String> commands = worldSection.getStringList("COMMANDS.LIST");

        if (commands.isEmpty()) return;

        String ip = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "127.0.0.1";

        for (String command : commands) {
            String processed = command
                    .replace("{Player}", player.getName())
                    .replace("{Uuid}", player.getUniqueId().toString())
                    .replace("{Address}", ip);

            String coloredCommand = addColor.setColors(processed);

            if (type.equalsIgnoreCase("CONSOLE")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), coloredCommand);
            } else {
                Bukkit.dispatchCommand(player, coloredCommand);
            }
        }
    }
}