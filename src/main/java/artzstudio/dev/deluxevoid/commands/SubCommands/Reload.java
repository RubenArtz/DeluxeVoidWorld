package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.WorldSettingsHandler;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Reload() {
        super("reload;rl", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        plugin.getReloadPlugin();

        WorldSettingsHandler.updateAlwaysDayCache();

        if (sender instanceof Player) {
            UtilityFunctions.executeSound("LEVEL_UP, 1.0f, 1.0f", (Player) sender);
        }
        for (String message : plugin.getFileTranslations().getStringList("MESSAGE_COMMAND_RELOAD")) {
            sender.sendMessage(addColor.setColors(message));
        }
    }
}
