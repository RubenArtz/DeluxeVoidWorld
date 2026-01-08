package artzstudio.dev.deluxevoid.events.inventory;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.events.chat.playerInteractEvent;
import artzstudio.dev.deluxevoid.session.SessionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryClose implements Listener {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        stopCreationSession((Player) event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        stopCreationSession(event.getPlayer());
    }

    private void stopCreationSession(Player player) {
        if (plugin.getSessionManager().isInMode(player.getUniqueId(), SessionManager.SessionMode.CREATE_WORLD_CHAT)) {

            plugin.removeMessages();
            plugin.getSessionManager().clearMode(player.getUniqueId());

            playerInteractEvent.removeTask(player.getUniqueId());
        }
    }
}