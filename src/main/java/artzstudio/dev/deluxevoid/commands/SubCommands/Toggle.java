package artzstudio.dev.deluxevoid.commands.SubCommands;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.SubCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

public class Toggle extends SubCommand {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

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
                UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.SOUND_THERE_IS_NO_WORLD")), (Player) sender);
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            } else {
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_UNKNOWN_WORLD_COMMAND").replace("{World}", bossbar)));
            }
        } else {
            YamlDocument worlds = plugin.getWorlds();
            String path = "WORLDS." + bossbar + ".TP-WHEN-FALLING";
            boolean currentState = worlds.getBoolean(path, false);

            worlds.set(path, !currentState);
            try {
                worlds.save();
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }

            if (!currentState) {
                if (sender instanceof Player) {
                    String sound = plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.ENABLED_VOID_TP");
                    if (sound != null) UtilityFunctions.executeSound(sound, (Player) sender);
                }
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_ACTIVATED_WORLD")).replace("{World}", bossbar));
            } else {
                if (sender instanceof Player) {
                    String sound = plugin.getConfigYaml().getString("ADMIN-CONFIG.SOUNDS.DISABLED_VOID_TP");
                    if (sound != null) UtilityFunctions.executeSound(sound, (Player) sender);
                }
                sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_DISABLED_WORLD")).replace("{World}", bossbar));
            }
        }
    }
}
