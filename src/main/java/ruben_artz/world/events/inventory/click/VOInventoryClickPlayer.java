package ruben_artz.world.events.inventory.click;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.menu.Player;
import ruben_artz.world.utils.ProjectUtils;

import java.util.Objects;

public class VOInventoryClickPlayer implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    /*
     * Listener of the VOPlayer class
     */

    @EventHandler
    public void getInventoryOptions(InventoryClickEvent event) {
        String name = ChatColor.stripColor(Player.title);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(name)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else {
                if (event.getCurrentItem().hasItemMeta()) {
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player)event.getWhoClicked();
                    event.setCancelled(true);
                    if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.TELEPORTATION.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Teleportation")) {
                            if (plugin.getIgnoreTeleportation().contains(player.getUniqueId())) {
                                plugin.getIgnoreTeleportation().remove(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, true, false, false, false);
                            } else {
                                plugin.getIgnoreTeleportation().add(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", false);
                            }
                            ProjectUtils.syncDelayedTask(3L, () -> Player.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    }  else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.JUMP.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Jumping")) {
                            if (plugin.getIgnoreJumping().contains(player.getUniqueId())) {
                                plugin.getIgnoreJumping().remove(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, true, false, false);
                            } else {
                                plugin.getIgnoreJumping().add(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", false);
                            }
                           ProjectUtils.syncDelayedTask(3L, () -> Player.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    } else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.LIGHTNING.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Lightning")) {
                            if (plugin.getIgnoreLightning().contains(player.getUniqueId())) {
                                plugin.getIgnoreLightning().remove(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, false, true, false);
                            } else {
                                plugin.getIgnoreLightning().add(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", false);
                            }
                           ProjectUtils.syncDelayedTask(3L, () -> Player.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
                            }
                        }
                    } else if (event.getSlot() == plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.PARTICLES.BOOLEAN")) {
                        if (player.hasPermission("DeluxeVoidWorld.Toggle.Particles")) {
                            if (plugin.getIgnoreParticles().contains(player.getUniqueId())) {
                                plugin.getIgnoreParticles().remove(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.ENABLED_ITEM")), player);
                                Launcher.getCache().updateBool(player, false, false, false, true);
                            } else {
                                plugin.getIgnoreParticles().add(player.getUniqueId());
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.DISABLED-ITEM")), player);
                                Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", false);
                            }
                            ProjectUtils.syncDelayedTask(3L, () -> Player.getInventory(player));
                        } else {
                            if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
                                ProjectUtils.executeSound(Objects.requireNonNull(plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION")), player);
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
