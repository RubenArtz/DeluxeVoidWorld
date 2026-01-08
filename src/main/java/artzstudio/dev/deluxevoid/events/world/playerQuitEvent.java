package artzstudio.dev.deluxevoid.events.world;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class playerQuitEvent implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onPlayerQuitEffects(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (plugin.getPlayerManager() != null) {
            plugin.getPlayerManager().removePlayer(uuid);
        }

        if (plugin.getSessionManager() != null) {
            plugin.getSessionManager().clearAll(uuid);
        }
    }
}