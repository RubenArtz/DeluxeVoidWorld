package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.utils.addColor;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;
import ruben_artz.world.utils.ProjectUtils;

public class Reload extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Reload() {
        super("reload;rl", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        plugin.getReloadPlugin();
        if (sender instanceof Player) {
            ProjectUtils.executeSound("LEVEL_UP, 1.0f, 1.0f", (Player) sender);
        }
        for (String message : plugin.getFileTranslations().getStringList("MESSAGE_COMMAND_RELOAD")) {
            sender.sendMessage(addColor.setColors(message));
        }
    }
}
