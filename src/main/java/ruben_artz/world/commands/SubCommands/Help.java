package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;
import ruben_artz.world.utils.ProjectUtils;

public class Help extends SubCommand {
    public Help() {
        super("help;helpme", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            ProjectUtils.getHelpCommandGame((Player) sender);
        } else {
            ProjectUtils.getHelpCommandConsole(sender);
        }
    }
}
