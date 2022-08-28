package ruben_artz.world.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {
    private final String[] names;
    private final String permission;
    protected CommandSender sender;

    public SubCommand(String name, String permission) {
        this.names = name.split(";");
        this.permission = permission;
    }
    public final List<String> getNames() {
        return Arrays.asList(names);
    }
    public final String getPermission() {
        return this.permission;
    }
    public abstract void onCommands(final CommandSender sender, final String[] args);
}
