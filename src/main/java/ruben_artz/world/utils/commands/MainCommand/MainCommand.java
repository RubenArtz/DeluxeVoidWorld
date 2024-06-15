package ruben_artz.world.utils.commands.MainCommand;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ruben_artz.world.features.addColor;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @Getter private final String permission;
    private final int reqArgs;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public MainCommand(String name, int param, SubCommand... subCommands) {
        this.permission = name;
        this.reqArgs = param - 1;
        for (int length = subCommands.length, i = 0; i < length; ++i)
            this.subCommands.add(subCommands[i]);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] array) {
        if (reqArgs <= -1) {
            onCommand(sender, array);
            return false;
        }
        if (array.length > reqArgs) {
            try {
                for (SubCommand subCommand : subCommands) {
                    if (subCommand.getNames().contains(array[0].toLowerCase())) {
                        if (!sender.hasPermission(subCommand.getPermission())) {
                            addColor.sendMessage((Player) sender, plugin.getFileTranslations().getString("MESSAGE_NO_PERMISSIONS"));
                            return false;
                        }
                        subCommand.onCommands(subCommand.sender = sender, array);
                        return false;
                    }
                }
                if (sender instanceof Player) {
                    ProjectUtils.getMessagesArgs((Player) sender);
                } else {
                    ProjectUtils.getMessagesArgsConsole(sender);
                }
            } catch (NullPointerException exception) {
                onCommand(sender, array);
            }
            return false;
        }
        if (sender instanceof Player) {
            ProjectUtils.getMessagesArgs((Player) sender);
        } else {
            ProjectUtils.getMessagesArgsConsole(sender);
        }
        return false;
    }

    @Nullable @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (!commandSender.hasPermission("DeluxeAnnounce.Admin")) {
            return null;
        }
        if (args.length == 1) {
            String partialCommand = args[0];
            /*
            General
             */
            commands.add("help");
            commands.add("reload");
            commands.add("editor");
            commands.add("toggle");
            commands.add("setworldvoid");
            commands.add("teleport");
            StringUtil.copyPartialMatches(partialCommand, commands, completions);
        } else if (args.length == 2) {
            String partialCommand = args[1];
            if (args[0].equalsIgnoreCase("toggle")) {
                for (World world : Bukkit.getWorlds()) {
                    commands.add(world.getName());
                }
                StringUtil.copyPartialMatches(partialCommand, commands, completions);
            } else if (args[0].equalsIgnoreCase("toggletp")) {
                for (World world : Bukkit.getWorlds()) {
                    commands.add(world.getName());
                }
                StringUtil.copyPartialMatches(partialCommand, commands, completions);
            }  else if (args[0].equalsIgnoreCase("toggleworld")) {
                for (World world : Bukkit.getWorlds()) {
                    commands.add(world.getName());
                }
                StringUtil.copyPartialMatches(partialCommand, commands, completions);
            } else {
                if (args[0].equalsIgnoreCase("teleport")) {
                    for (World world : Bukkit.getWorlds()) {
                        commands.add(world.getName());
                    }
                    StringUtil.copyPartialMatches(partialCommand, commands, completions);
                } else if (args[0].equalsIgnoreCase("tp")) {
                    for (World world : Bukkit.getWorlds()) {
                        commands.add(world.getName());
                    }
                    StringUtil.copyPartialMatches(partialCommand, commands, completions);
                }
                else if (args[0].equalsIgnoreCase("tpme")) {
                    for (World world : Bukkit.getWorlds()) {
                        commands.add(world.getName());
                    }
                    StringUtil.copyPartialMatches(partialCommand, commands, completions);
                }
            }
        } else {
            return ImmutableList.of();
        }
        Collections.sort(completions);
        return completions;
    }

    private void onCommand(CommandSender sender, String[] array) {}
}