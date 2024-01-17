package ruben_artz.world.events.inventory.click;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.menu.VOPlayer;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

public class VOInventoryClickPlayer implements Listener {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);

    /*
     * Listener of the VOPlayer class
     */

    @EventHandler
    public void getInventoryOptions(InventoryClickEvent event) {
        String name = ChatColor.stripColor(VOPlayer.title);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    Player player = (Player)event.getWhoClicked();
                    event.setCancelled(true);
                    if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.TELEPORTATION.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Teleportation")) {
                            if (plugin.getIgnoreTeleportation().contains(player.getUniqueId())) {
                                plugin.getIgnoreTeleportation().remove(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, true, false, false, false);
                            } else {
                                plugin.getIgnoreTeleportation().add(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", false);
                            }
                            VOManager.syncDelayedTask(3L, () -> VOPlayer.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                VOManager.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    }  else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.JUMP.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Jumping")) {
                            if (plugin.getIgnoreJumping().contains(player.getUniqueId())) {
                                plugin.getIgnoreJumping().remove(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, true, false, false);
                            } else {
                                plugin.getIgnoreJumping().add(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", false);
                            }
                           VOManager.syncDelayedTask(3L, () -> VOPlayer.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                VOManager.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    } else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.LIGHTNING.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Lightning")) {
                            if (plugin.getIgnoreLightning().contains(player.getUniqueId())) {
                                plugin.getIgnoreLightning().remove(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, false, true, false);
                            } else {
                                plugin.getIgnoreLightning().add(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", false);
                            }
                           VOManager.syncDelayedTask(3L, () -> VOPlayer.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                VOManager.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    } else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.PARTICLES.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Particles")) {
                            if (plugin.getIgnoreParticles().contains(player.getUniqueId())) {
                                plugin.getIgnoreParticles().remove(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, false, false, true);
                            } else {
                                plugin.getIgnoreParticles().add(player.getUniqueId());
                                VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", false);
                            }
                            VOManager.syncDelayedTask(3L, () -> VOPlayer.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                VOManager.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    } else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.CLOSE.SLOT")) {
                        player.closeInventory();
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
