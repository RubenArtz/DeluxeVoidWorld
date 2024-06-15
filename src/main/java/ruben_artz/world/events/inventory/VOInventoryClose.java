package ruben_artz.world.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ruben_artz.world.events.chat.VOEditing;
import ruben_artz.world.menu.Home;
import ruben_artz.world.DeluxeVoidWorld;

public class VOInventoryClose implements Listener {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    /*
    Cancel the task of the items to be updated
     */
    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        /*
         * Cancel main menu events
         */
        if (event.getInventory().equals(Home.getInventory())) {
            if (Home.task != null) {
                Home.task.cancel();
            }
            plugin.removeInventory(player.getName());
            Home.itemStacks.clear();
        }
        /*
         * Cancel events when inventory is opened to create worlds
         */
        if (plugin.getCreate_world().contains(player.getUniqueId())) {
            plugin.removeMessages();
            plugin.getCreate_world().remove(player.getUniqueId());
            VOEditing.create.cancel();
        }
    }

    /*
     * Remove the player's actions again
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        /*
         * Cancel main menu events
         */
        if (Home.task != null) {
            Home.task.cancel();
        }
        plugin.removeInventory(player.getName());
        Home.itemStacks.clear();

        /*
         * Cancel events when inventory is opened to create worlds
         */
        if (plugin.getCreate_world().contains(player.getUniqueId())) {
            plugin.removeMessages();
            plugin.getCreate_world().remove(player.getUniqueId());
            VOEditing.create.cancel();
        }
        plugin.removeMessages();
    }
}