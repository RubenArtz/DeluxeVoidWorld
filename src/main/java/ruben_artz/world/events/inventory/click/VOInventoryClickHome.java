package ruben_artz.world.events.inventory.click;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ruben_artz.world.events.chat.VOEditing;
import ruben_artz.world.features.addColor;
import ruben_artz.world.features.sendTitles;
import ruben_artz.world.menu.VOHome;
import ruben_artz.world.menu.VOIcon;
import ruben_artz.world.world.VOArrays;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class VOInventoryClickHome implements Listener {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);

    /*
     * Event when clicking on normal items
     */
    @EventHandler
    public void getCreate(InventoryClickEvent event) {
        String name = ChatColor.stripColor(VOHome.title);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    int slot = event.getSlot();
                    VOArrays inventory = plugin.getInventory(player.getName());
                    if (inventory != null) {
                        if (slot == plugin.getMenuHome().getInt("MAIN.ADD_WORLD.SLOT")) {
                            plugin.chat.add(player.getUniqueId());
                            player.closeInventory();
                            VOEditing.announce = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (String Titlelist : plugin.getFileTranslations().getStringList("MESSAGE_CREATE_WORLD")) {
                                        String[] Title = Titlelist.split(";");
                                        sendTitles.sendTitle(player, Integer.parseInt(Title[0]), Integer.parseInt(Title[1]), Integer.parseInt(Title[2]), Title[3], Title[4]);
                                    }
                                }
                            }.runTaskAsynchronously(plugin), 0L, 50L);
                        } else if (slot == plugin.getMenuHome().getInt("MAIN.CLOSE.SLOT")) {
                            player.closeInventory();
                        } else if (slot == plugin.getMenuHome().getInt("MAIN.RELOAD.SLOT")) {
                            Bukkit.dispatchCommand(player, "dew reload");
                        } else if (slot == plugin.getMenuHome().getInt("MAIN.CREATE_WORLD.SLOT")) {
                            player.closeInventory();
                            VOEditing.create = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
                                for (String title : plugin.getFileTranslations().getStringList("MESSAGE_CREATE_WORLD_EMPTY")) {
                                    String[] list = title.split(";");
                                    sendTitles.sendTitle(player, Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]), list[3], list[4]);
                                }
                            }, 0L, 50L);
                            VOManager.syncTaskLater(5L, () -> plugin.create_world.add(player.getUniqueId()));
                        } else if (slot == plugin.getMenuHome().getInt("MAIN.NEXT.SLOT") && event.getCurrentItem().getType().equals(XMaterial.valueOf(plugin.getMenuHome().getString("MAIN.NEXT.MATERIAL")).parseMaterial())) {
                            int currentPage = inventory.getPage();
                            int newPage = currentPage + 1;
                            if (VOHome.task != null) {
                                VOHome.task.cancel();
                            }
                            plugin.removeInventory(player.getName());
                            VOHome.getInventory(player, newPage);
                        } else if (slot == plugin.getMenuHome().getInt("MAIN.RETURN.SLOT") && event.getCurrentItem().getType().equals(XMaterial.valueOf(plugin.getMenuHome().getString("MAIN.RETURN.MATERIAL")).parseMaterial())) {
                            int currentPage = inventory.getPage();
                            int newPage = currentPage - 1;
                            if (VOHome.task != null) {
                                VOHome.task.cancel();
                            }
                            plugin.removeInventory(player.getName());
                            VOHome.getInventory(player, newPage);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    /*
     * Event when clicking on created books (Maps of world.yml)
     */
    @EventHandler
    public void getInventory(InventoryClickEvent event) {
        String name = ChatColor.stripColor(VOHome.title);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    int slot = 0;
                    VOArrays inventory = plugin.getInventory(player.getName());
                    if (inventory != null) {
                        for (String world : Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false)) {
                            if (event.getInventory().equals(player.getOpenInventory().getTopInventory())) {
                                if (event.getSlot() == slot) {
                                    String[] worldName = Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + world + ".SPAWN")).split(",");
                                    World world1 = Bukkit.getWorld(worldName[0]);
                                    if (event.getClick().equals(ClickType.LEFT)) {
                                        plugin.getWorlds().set("WORLDS." + world, null);
                                        plugin.files.saveFile("worlds.yml");
                                        plugin.initiate();
                                        XSound.play(player, plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DELETED_WORLD_MENU"));
                                        addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_DELETE_WORLD"));
                                        if (VOHome.task != null) {
                                            VOHome.task.cancel();
                                        }
                                        plugin.removeInventory(player.getName());
                                        VOHome.getInventory(player, 1);
                                    } else if (event.getClick().equals(ClickType.RIGHT)) {
                                        if (!plugin.getWorlds().getBoolean("WORLDS." + world + ".TP-WHEN-FALLING")) {
                                            plugin.getWorlds().set("WORLDS." + world + ".TP-WHEN-FALLING", true);
                                            plugin.files.saveFile("worlds.yml");
                                            plugin.initiate();
                                            XSound.play(player, plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_VOID_TP"));
                                        } else {
                                            plugin.getWorlds().set("WORLDS." + world + ".TP-WHEN-FALLING", false);
                                            plugin.files.saveFile("worlds.yml");
                                            plugin.initiate();
                                            XSound.play(player, plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED_VOID_TP"));
                                        }
                                        player.updateInventory();
                                    } else if (event.getClick().equals(ClickType.DROP)) {
                                        if (player.getWorld().equals(world1)) {
                                            Bukkit.dispatchCommand(player, "dew setworldvoid");
                                        } else {
                                            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NOT_USE_ITEM"));
                                        }
                                    } else if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                                        String[] names = Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + world + ".SPAWN")).split(",");
                                        Bukkit.dispatchCommand(player, "dew teleport " + names[0] + "");
                                    } else if (event.getClick() == ClickType.MIDDLE) {
                                        if (player.getWorld().equals(world1)) {
                                            plugin.chat_get.add(player.getUniqueId());
                                            player.closeInventory();
                                            VOEditing.getBlockX = VOManager.syncRepeatingTask(50L, () -> VOManager.synTaskAsynchronously(() -> {
                                                for (String Titlelist : plugin.getFileTranslations().getStringList("MESSAGE_EDIT_BLOCK_Y")) {
                                                    String[] Title = Titlelist.split(";");
                                                    sendTitles.sendTitle(player, Integer.parseInt(Title[0]), Integer.parseInt(Title[1]), Integer.parseInt(Title[2]), Title[3], Title[4]);
                                                }
                                            }));
                                        } else {
                                            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NOT_USE_ITEM"));
                                        }
                                    } else if (event.getClick().equals(ClickType.SHIFT_LEFT) || (event.getClick().equals(ClickType.SHIFT_RIGHT))) {
                                        if (player.getWorld().equals(world1)) {
                                            VOIcon.getInventory(player);
                                        } else {
                                            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NOT_USE_ITEM"));
                                        }
                                    } else {
                                        event.setCancelled(true);
                                    }
                                }
                            }
                            slot++;
                            if (slot > 45) {
                                break;
                            }
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
