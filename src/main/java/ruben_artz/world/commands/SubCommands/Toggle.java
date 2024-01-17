package ruben_artz.world.commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.commands.SubCommand;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

public class Toggle extends SubCommand {
    private final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public Toggle() {
        super("toggle;toggletp;toggleworld", "DeluxeVoidWorld.Admin");
    }

    @Override
    public void onCommands(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_USE_TOGGLE")));
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
            if (!plugin.getWorlds().getBoolean("WORLDS." + bossbar + ".TP-WHEN-FALLING")) {
                plugin.getWorlds().set("WORLDS." + bossbar + ".TP-WHEN-FALLING", true);
                plugin.files.saveFile("worlds.yml");
                if (sender instanceof Player) {
                    VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_VOID_TP")), (Player) sender);
                }
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_ACTIVATED_WORLD")).replace("{World}", bossbar));
            } else {
                plugin.getWorlds().set("WORLDS." + bossbar + ".TP-WHEN-FALLING", false);
                plugin.files.saveFile("worlds.yml");
                if (sender instanceof Player) {
                    VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED_VOID_TP")), (Player) sender);
                }
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_DISABLED_WORLD")).replace("{World}", bossbar));
            }
        }
    }
}
