package ruben_artz.world.commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.UtilityFunctions;
import ruben_artz.world.utils.addColor;
import ruben_artz.world.utils.commands.MainCommand.SubCommand;

public class SetVoid extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public SetVoid() {
        super("setworldvoid;setspawn;spawn", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            plugin.getWorlds().set("WORLDS." + ((Player) sender).getWorld().getName() + ".SPAWN", UtilityFunctions.setLocation(((Player) sender).getLocation()));
            plugin.files.saveFile("worlds.yml");
            for (String message : plugin.getFileTranslations().getStringList("MESSAGE_ADD_WORLD")) {
                message = UtilityFunctions.replacePlaceholder(message, "{World}", ((Player) sender).getWorld().getName());
                message = UtilityFunctions.replacePlaceholder(message, "{X}", String.valueOf(((Player) sender).getLocation().getBlockX()));
                message = UtilityFunctions.replacePlaceholder(message, "{Y}", String.valueOf(((Player) sender).getLocation().getBlockY()));
                message = UtilityFunctions.replacePlaceholder(message, "{Z}", String.valueOf(((Player) sender).getLocation().getBlockZ()));
                message = UtilityFunctions.replacePlaceholder(message, "{YAW}", String.valueOf(((Player) sender).getLocation().getYaw()));
                message = UtilityFunctions.replacePlaceholder(message, "{PITCH}", String.valueOf(((Player) sender).getLocation().getPitch()));
                sender.sendMessage(addColor.setColors(UtilityFunctions.setPlaceholders((Player) sender, message)));
            }
            UtilityFunctions.executeSound("LEVEL_UP, 1.0f, 1.0f", (Player) sender);
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND")));
        }
    }
}
