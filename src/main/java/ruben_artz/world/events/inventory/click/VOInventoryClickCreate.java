package ruben_artz.world.events.inventory.click;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.events.chat.VOString;
import ruben_artz.world.menu.Create;
import ruben_artz.world.menu.Home;
import ruben_artz.world.utils.UtilityFunctions;
import ruben_artz.world.utils.Slime;

import java.util.ArrayList;

public class VOInventoryClickCreate implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    /*
     * Event when the player creates a world
     */
    @EventHandler
    public void getCreates(InventoryClickEvent event) {
        String name = ChatColor.stripColor(Create.title);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    ArrayList<VOString> messages = plugin.getMessage();
                    VOString message = messages.get(0);
                    int slot = event.getSlot();
                    if (slot == plugin.getInventoryCrating().getInt("MAIN.NORMAL.SLOT")) {
                        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
                            Slime.createWorldWithSlime(player, message.getName(), "NORMAL");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("Multiverse-Core")) {
                            UtilityFunctions.createWorldWithMultiverse(player, message.getName(), "NORMAL");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("UltraRegions")) {
                            UtilityFunctions.createUltraWorld(player, message.getName(), "NORMAL");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else {
                            UtilityFunctions.createWorldsDeluxe(player, message.getName(), "NORMAL");
                            player.closeInventory();
                            plugin.removeMessages();
                        }
                    } else if (slot == plugin.getInventoryCrating().getInt("MAIN.NETHER.SLOT")) {
                        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
                            Slime.createWorldWithSlime(player, message.getName(), "NETHER");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("Multiverse-Core")) {
                            UtilityFunctions.createWorldWithMultiverse(player, message.getName(), "NETHER");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("UltraRegions")) {
                            UtilityFunctions.createUltraWorld(player, message.getName(), "NETHER");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else {
                            UtilityFunctions.createWorldsDeluxe(player, message.getName(), "NETHER");
                            player.closeInventory();
                            plugin.removeMessages();
                        }
                    } else if (slot == plugin.getInventoryCrating().getInt("MAIN.THE_END.SLOT")) {
                        if (UtilityFunctions.isPluginEnabled("SlimeWorldManager")) {
                            UtilityFunctions.createWorldWithMultiverse(player, message.getName(), "THE_END");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("Multiverse-Core")) {
                            UtilityFunctions.createWorldsDeluxe(player, message.getName(), "THE_END");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else if (UtilityFunctions.isPluginEnabled("UltraRegions")) {
                            UtilityFunctions.createUltraWorld(player, message.getName(), "THE_END");
                            player.closeInventory();
                            plugin.removeMessages();
                        } else {
                            UtilityFunctions.createWorldsDeluxe(player, message.getName(), "THE_END");
                            player.closeInventory();
                            plugin.removeMessages();
                        }
                    } else if (slot == plugin.getInventoryCrating().getInt("MAIN.CLOSE.SLOT")) {
                        player.closeInventory();
                    } else if (slot == plugin.getInventoryCrating().getInt("MAIN.RETURN.SLOT")) {
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
