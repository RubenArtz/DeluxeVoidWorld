package ruben_artz.world.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;

public class VOPlayer implements CommandExecutor {
    private final VOMain plugin = VOMain.getPlugin(VOMain.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
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
