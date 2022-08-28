package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.commands.SubCommand;
import ruben_artz.world.menu.VOHome;

public class Editor extends SubCommand {
    private final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public Editor() {
        super("editor;inventory;inv", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            VOHome.getInventory(((Player) sender), 1);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_MENU_COMMAND")));
        }
    }
}
