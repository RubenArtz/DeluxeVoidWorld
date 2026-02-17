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

package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Teleport extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Teleport() {
        super("teleport;tp;tpme", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_USE_TELEPORT")));
            return;
        }
        String bossbar = String.join(" ", args);
        bossbar = bossbar.replace(args[0] + " ", "");
        World world = Bukkit.getWorld(args[1]);
        if (world == null) {
            if (sender instanceof Player) {
                UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.SOUND_THERE_IS_NO_WORLD")), (Player) sender);
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            } else {
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            }
        } else {
            if (sender instanceof Player) {
                ((Player) sender).teleport(world.getSpawnLocation());
                ((Player) sender).setAllowFlight(true);
                ((Player) sender).setFlying(true);
                sender.sendMessage(addColor.setColors(UtilityFunctions.setPlaceholders((Player) sender, plugin.getFileTranslations().getString("MESSAGE_TP_WORLD").replace("{World}", bossbar))));
            } else {
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND")));
            }
        }
    }
}
