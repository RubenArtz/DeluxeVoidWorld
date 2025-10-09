package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.utils.CrossPlatformUtils;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;

public class Help extends SubCommand {
    public Help() {
        super("help;helpme", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            CrossPlatformUtils.getHelpCommandGame((Player) sender);
        } else {
            CrossPlatformUtils.getHelpCommandConsole(sender);
        }
    }
}
