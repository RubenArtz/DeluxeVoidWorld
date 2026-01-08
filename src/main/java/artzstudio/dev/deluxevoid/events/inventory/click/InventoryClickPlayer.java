package artzstudio.dev.deluxevoid.events.inventory.click;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.data.player.PlayerData;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.menu.PlayerMenu;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickPlayer implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void getInventoryOptions(InventoryClickEvent event) {
        YamlDocument config = plugin.getBoolean();

        String expectedTitle = ChatColor.stripColor(addColor.setColors(config.getString("MAIN.TITLE", "Option Selector")));

        if (!ChatColor.stripColor(event.getView().getTitle()).equals(expectedTitle)) return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        PlayerData data = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (slot == config.getInt("MAIN.BOOLEAN.SLOTS.TELEPORTATION.BOOLEAN")) {
            if (checkPerm(player, "Teleportation")) {
                boolean newState = !data.isTeleportEnabled();
                data.setTeleportEnabled(newState);
                Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", newState);

                if (newState) {
                    data.setJumpEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", false);
                    data.setLightningEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", false);
                    data.setParticlesEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", false);
                }

                playSound(player, newState);
                refresh(player);
            }
        } else if (slot == config.getInt("MAIN.BOOLEAN.SLOTS.JUMP.BOOLEAN")) {
            if (checkPerm(player, "Jumping")) {
                boolean newState = !data.isJumpEnabled();
                data.setJumpEnabled(newState);
                Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", newState);

                if (newState) {
                    data.setTeleportEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", false);
                    data.setLightningEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", false);
                    data.setParticlesEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", false);
                }

                playSound(player, newState);
                refresh(player);
            }
        } else if (slot == config.getInt("MAIN.BOOLEAN.SLOTS.LIGHTNING.BOOLEAN")) {
            if (checkPerm(player, "Lightning")) {
                boolean newState = !data.isLightningEnabled();
                data.setLightningEnabled(newState);
                Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", newState);

                if (newState) {
                    data.setTeleportEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", false);
                    data.setJumpEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", false);
                    data.setParticlesEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", false);
                }

                playSound(player, newState);
                refresh(player);
            }
        } else if (slot == config.getInt("MAIN.BOOLEAN.SLOTS.PARTICLES.BOOLEAN")) {
            if (checkPerm(player, "Particles")) {
                boolean newState = !data.isParticlesEnabled();
                data.setParticlesEnabled(newState);
                Launcher.getCache().setUpdate(player.getUniqueId(), "PARTICLES", newState);

                if (newState) {
                    data.setTeleportEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "TELEPORT", false);
                    data.setJumpEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "JUMP", false);
                    data.setLightningEnabled(false);
                    Launcher.getCache().setUpdate(player.getUniqueId(), "LIGHTNING", false);
                }

                playSound(player, newState);
                refresh(player);
            }
        } else if (slot == config.getInt("MAIN.CLOSE.SLOT")) {
            player.closeInventory();
        }
    }

    private boolean checkPerm(Player player, String suffix) {
        if (player.hasPermission("DeluxeVoidWorld.Toggle." + suffix)) return true;

        if (plugin.getBoolean().getBoolean("MAIN.PLAYER.SOUNDS.PERMISSION.ENABLED")) {
            String sound = plugin.getBoolean().getString("MAIN.PLAYER.SOUNDS.PERMISSION.NO-PERMISSION");
            if (sound != null) UtilityFunctions.executeSound(sound, player);
        }
        return false;
    }

    private void playSound(Player player, boolean enabled) {
        String path = enabled ? "ADMIN-CONFIG.SOUNDS.ENABLED_ITEM" : "ADMIN-CONFIG.SOUNDS.DISABLED-ITEM";
        String sound = plugin.getConfigYaml().getString(path);
        if (sound != null) UtilityFunctions.executeSound(sound, player);
    }

    private void refresh(Player player) {
        UtilityFunctions.runTaskLater(3L, () -> PlayerMenu.getInventory(player));
    }
}