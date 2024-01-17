package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.commands.SubCommand;
import ruben_artz.world.world.VOManager;

public class Reload extends SubCommand {
    private final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public Reload() {
        super("reload;rl", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        plugin.getReloadPlugin();
        if (sender instanceof Player) {
            VOManager.executeSound("LEVEL_UP, 1.0f, 1.0f", (Player) sender);
        }
        for (String message : plugin.getFileTranslations().getStringList("MESSAGE_COMMAND_RELOAD")) {
            sender.sendMessage(addColor.setColors(message));
        }
    }
}
