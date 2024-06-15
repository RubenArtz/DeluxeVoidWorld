package ruben_artz.world.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.DeluxeVoidWorld;

public class VOPlayer implements CommandExecutor {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String string, String[] args) {
        if (!sender.hasPermission("DeluxeVoidWorld.Manager")) {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_PERMISSIONS_GUI")));
            return true;
        }
        if (sender instanceof Player) {
            ruben_artz.world.menu.VOPlayer.getInventory((Player) sender);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_MENU_COMMAND")));
        }
        return false;
    }
}
