package ruben_artz.world.events;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.UtilityFunctions;
import ruben_artz.world.utils.NotificationManager;
import ruben_artz.world.utils.addColor;

public class updateEvent implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    private final String USER_ID = "%%__USER__%%";

    @EventHandler
    public void checkPlayer(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Audience audience = plugin.getAudiences(player);

         /*
        Ruben_Artz check update.
         */
        if ("Ruben_Artz".equals(player.getName())) {
            for (int i = 0; i < 5; i++) {
                player.sendMessage("");
            }

            UtilityFunctions.runTaskLater(40, () -> {
                try {
                    String latestVersion = NotificationManager.fetchLatestVersion();
                    if (latestVersion == null) {
                        return;
                    }

                    audience.sendMessage(addColor.addColors(player,
                            "&8« » ==== &e✯ &9&lDeluxe Void World &e✯ &8==== « »"));
                    audience.sendMessage(addColor.addColors(player,
                            "&f"));
                    audience.sendMessage(addColor.addColors(player,
                            "<dark_gray>•</dark_gray> <hover:show_text:'<white>Última versión:</white> <green>" + latestVersion + "</green>'><white>Versión:</white> <green>" + plugin.version + "</green></hover>"));

                    audience.sendMessage(addColor.addColors(player,
                            "<dark_gray>•</dark_gray> <yellow>SpigotMC</yellow>: <yellow><u><click:open_url:'https://www.spigotmc.org/members/" + USER_ID + "/'><hover:show_text:'Haga click aquí para ir al perfil.'>spigotmc.org/members/" + USER_ID + "/</hover></click></u></yellow>"));

                    audience.sendMessage(addColor.addColors(player,
                            "<dark_gray>•</dark_gray> <color:#16a667>Polymart</color>: <color:#16a667><u><click:open_url:'https://polymart.org/user/" + USER_ID + "/'><hover:show_text:'Haga click aquí para ir al perfil.'>polymart.org/user/" + USER_ID + "/</hover></click></u></color>"));

                    audience.sendMessage(addColor.addColors(player,
                            "&f"));
                    audience.sendMessage(addColor.addColors(player,
                            "&8==================================="));
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to check for updates: " + e.getMessage());
                }

            });
        } else {
            player.getUniqueId();
        }

        if (!player.isOp() && !player.hasPermission("*")) {
            return;
        }

        UtilityFunctions.runTaskLater(100L, () -> checkForUpdates(audience));
    }

    private void checkForUpdates(Audience audience) {
        try {
            String latestVersion = NotificationManager.fetchLatestVersion();
            if (latestVersion == null) {
                return;
            }

            String currentVersion = plugin.getDescription().getVersion();

            if (!currentVersion.equals(latestVersion)) {
                audience.sendMessage(addColor.addColors("&a[" + plugin.getDescription().getName() + "] There is a newer plugin version available: &a&l"
                        + latestVersion + "&a, you're on: &a&l" + plugin.getDescription().getVersion()));

            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to check for updates: " + e.getMessage());
        }
    }
}
