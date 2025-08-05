package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.menu.Home;
import ruben_artz.world.utils.addColor;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;

public class Editor extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Editor() {
        super("editor;inventory;inv", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Home.getInventory(((Player) sender), 1);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_MENU_COMMAND")));
        }
    }
}
