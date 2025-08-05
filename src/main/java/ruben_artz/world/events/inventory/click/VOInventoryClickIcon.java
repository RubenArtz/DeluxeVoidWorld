package ruben_artz.world.events.inventory.click;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.menu.Home;
import ruben_artz.world.menu.Icon;

import java.util.Objects;

public class VOInventoryClickIcon implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    /*
     * Event when clicking on the icon inventory.
     */
    @EventHandler
    public void getInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String name = ChatColor.stripColor(Icon.title.replace("{Name World}", player.getWorld().getName()));
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    int slot = 0;
                    for (String world : Objects.requireNonNull(plugin.getIcons().getConfigurationSection("MATERIALS")).getKeys(false)) {
                        if (event.getInventory().equals(player.getOpenInventory().getTopInventory())) {
                            if (event.getSlot() == slot) {
                                plugin.getWorlds().set("WORLDS." + player.getWorld().getName() + ".MATERIAL", plugin.getIcons().getString("MATERIALS." + world + ".ITEM"));
                                plugin.files.saveFile("worlds.yml");
                                Home.getInventory(player, 1);
                            } else {
                                event.setCancelled(true);
                            }
                        }
                        slot++;
                        if (slot > 44) {
                            break;
                        }
                    }
                    if (event.getSlot() == plugin.getIcons().getInt("MAIN.CLOSE.SLOT")) {
                        player.closeInventory();
                    } else if (event.getSlot() == plugin.getIcons().getInt("MAIN.RETURN.SLOT")) {
                        Home.getInventory(player, 1);
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
