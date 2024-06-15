package ruben_artz.world.commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.DeluxeVoidWorld;
import ruben_artz.world.commands.SubCommand;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

public class Teleport extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public Teleport() {
        super("teleport;tp;tpme", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_USE_TELEPORT")));
            return;
        }
        String bossbar = String.join(" ", args);
        bossbar = bossbar.replace(args[0] + " ", "");
        World world = Bukkit.getWorld(args[1]);
        if (world == null) {
            if (sender instanceof Player) {
                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.SOUND_THERE_IS_NO_WORLD")), (Player) sender);
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            } else {
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            }
        } else {
            if (sender instanceof Player) {
                ((Player)sender).teleport(world.getSpawnLocation());
                ((Player)sender).setAllowFlight(true);
                ((Player)sender).setFlying(true);
                sender.sendMessage(addColor.setColors(VOManager.setPlaceholders((Player) sender, plugin.getFileTranslations().getString("MESSAGE_TP_WORLD").replace("{World}", bossbar))));
            } else {
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_NO_WORLD_COMMAND")));
            }
        }
    }
}
