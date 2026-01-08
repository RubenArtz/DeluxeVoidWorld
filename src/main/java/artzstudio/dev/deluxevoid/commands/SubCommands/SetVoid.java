package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetVoid extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public SetVoid() {
        super("setworldvoid;setspawn;spawn", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            String worldName = player.getWorld().getName();

            YamlDocument worldsCfg = plugin.getWorlds();
            worldsCfg.set("WORLDS." + worldName + ".SPAWN", UtilityFunctions.setLocation(player.getLocation()));

            try {
                worldsCfg.save();
            } catch (IOException ignored) {
            }

            for (String message : plugin.getFileTranslations().getStringList("MESSAGE_ADD_WORLD")) {
                message = UtilityFunctions.replacePlaceholder(message, "{World}", worldName);
                message = UtilityFunctions.replacePlaceholder(message, "{X}", String.valueOf(player.getLocation().getBlockX()));
                message = UtilityFunctions.replacePlaceholder(message, "{Y}", String.valueOf(player.getLocation().getBlockY()));
                message = UtilityFunctions.replacePlaceholder(message, "{Z}", String.valueOf(player.getLocation().getBlockZ()));
                message = UtilityFunctions.replacePlaceholder(message, "{YAW}", String.valueOf(player.getLocation().getYaw()));
                message = UtilityFunctions.replacePlaceholder(message, "{PITCH}", String.valueOf(player.getLocation().getPitch()));

                sender.sendMessage(addColor.setColors(UtilityFunctions.setPlaceholders(player, message)));
            }

            UtilityFunctions.executeSound("LEVEL_UP, 1.0f, 1.0f", player);
        } else {
            String noWorldMsg = plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND", "&cEste comando solo es para jugadores.");
            sender.sendMessage(addColor.setColors(noWorldMsg));
        }
    }
}
