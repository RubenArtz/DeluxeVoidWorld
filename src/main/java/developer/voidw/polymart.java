package developer.voidw;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import ruben_artz.world.main.VOMain;

public class polymart {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public static void setLicense() {
        if (strings.isTrue()) {
            plugin.sendConsole(plugin.prefix + "&aYour license is valid, thank you.");
            return;
        }

        plugin.sendConsole("");
        plugin.sendConsole(plugin.prefix + "&c############## WARNING ##############");
        plugin.sendConsole(plugin.prefix);
        plugin.sendConsole(plugin.prefix + "&cFailed to validate license:");
        plugin.sendConsole(plugin.prefix + "&cYour license specified on the config is the default one, you must change it or the plugin won't run.");
        plugin.sendConsole(plugin.prefix + "&cTo get a license please first verify as buyer on our discord and visit https://dashboard.stn-studios.dev/ for get a license.");
        plugin.sendConsole(plugin.prefix);
        plugin.sendConsole(plugin.prefix + "&c-> Our discord:");
        plugin.sendConsole(plugin.prefix + "&chttps://stn-studios.dev/discord");
        plugin.sendConsole(plugin.prefix);
        plugin.sendConsole(plugin.prefix + "&c############## WARNING ##############");
        plugin.sendConsole("");

        HandlerList.unregisterAll(plugin);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}