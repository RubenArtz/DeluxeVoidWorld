package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help extends SubCommand {
    public Help() {
        super("help;helpme", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            UtilityFunctions.getHelpCommandGame((Player) sender);
        } else {
            UtilityFunctions.getHelpCommandConsole(sender);
        }
    }
}
