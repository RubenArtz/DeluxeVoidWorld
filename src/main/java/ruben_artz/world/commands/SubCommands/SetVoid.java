package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;
import ruben_artz.world.utils.ProjectUtils;

public class SetVoid extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public SetVoid() {
        super("setworldvoid;setspawn;spawn", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            plugin.getWorlds().set("WORLDS." + ((Player) sender).getWorld().getName()+".SPAWN", ProjectUtils.setLocation(((Player) sender).getLocation()));
            plugin.files.saveFile("worlds.yml");
            for (String message : plugin.getFileTranslations().getStringList("MESSAGE_ADD_WORLD")) {
                message = ProjectUtils.replacePlaceholder(message, "{World}", ((Player) sender).getWorld().getName());
                message = ProjectUtils.replacePlaceholder(message, "{X}", String.valueOf(((Player) sender).getLocation().getBlockX()));
                message = ProjectUtils.replacePlaceholder(message, "{Y}", String.valueOf(((Player) sender).getLocation().getBlockY()));
                message = ProjectUtils.replacePlaceholder(message, "{Z}", String.valueOf(((Player) sender).getLocation().getBlockZ()));
                message = ProjectUtils.replacePlaceholder(message, "{YAW}", String.valueOf(((Player) sender).getLocation().getYaw()));
                message = ProjectUtils.replacePlaceholder(message, "{PITCH}", String.valueOf(((Player) sender).getLocation().getPitch()));
                sender.sendMessage(addColor.setColors(ProjectUtils.setPlaceholders((Player) sender, message)));
            }
            ProjectUtils.executeSound("LEVEL_UP, 1.0f, 1.0f", (Player) sender);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND")));
        }
    }
}
