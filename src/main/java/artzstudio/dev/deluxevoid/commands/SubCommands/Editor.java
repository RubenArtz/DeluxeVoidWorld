package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Editor extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Editor() {
        super("editor;inventory;inv", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Launcher.getInstance().getHomeListener().openInventory((Player) sender);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_MENU_COMMAND")));
        }
    }
}
