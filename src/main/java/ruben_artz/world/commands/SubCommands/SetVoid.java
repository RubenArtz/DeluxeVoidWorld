package ruben_artz.world.commands.SubCommands;

import com.cryptomorin.xseries.XSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.commands.SubCommand;
import ruben_artz.world.world.VOManager;

public class SetVoid extends SubCommand {
    private final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public SetVoid() {
        super("setworldvoid;setspawn;spawn", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            plugin.getWorlds().set("WORLDS." + ((Player) sender).getWorld().getName()+".SPAWN", VOManager.setLocation(((Player) sender).getLocation()));
            plugin.files.saveFile("worlds.yml");
            for (String message : plugin.getFileTranslations().getStringList("MESSAGE_ADD_WORLD")) {
                message = VOManager.replacePlaceholder(message, "{World}", ((Player) sender).getWorld().getName());
                message = VOManager.replacePlaceholder(message, "{X}", String.valueOf(((Player) sender).getLocation().getBlockX()));
                message = VOManager.replacePlaceholder(message, "{Y}", String.valueOf(((Player) sender).getLocation().getBlockY()));
                message = VOManager.replacePlaceholder(message, "{Z}", String.valueOf(((Player) sender).getLocation().getBlockZ()));
                message = VOManager.replacePlaceholder(message, "{YAW}", String.valueOf(((Player) sender).getLocation().getYaw()));
                message = VOManager.replacePlaceholder(message, "{PITCH}", String.valueOf(((Player) sender).getLocation().getPitch()));
                sender.sendMessage(addColor.setColors(VOManager.setPlaceholders((Player) sender, message)));
            }
            XSound.play((Player) sender, "LEVEL_UP, 1.0f, 1.0f");
        } else {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND")));
        }
    }
}
