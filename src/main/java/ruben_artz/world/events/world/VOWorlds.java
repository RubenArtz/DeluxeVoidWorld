package ruben_artz.world.events.world;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

public class VOWorlds implements Listener {
    public final VOMain plugin = VOMain.getPlugin(VOMain.class);

    @SuppressWarnings("ConstantConditions")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoveTeleportation(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();
        if (event.getTo().getBlockY() > plugin.getWorlds().getInt("WORLDS." + worldName + ".VOID-POSITION", plugin.getWorlds().getInt("WORLDS." + worldName + ".VOID-POSITION")) + 15) {
            return;
        }
        setEffect("ADD", player);
        if (event.getTo().getBlockY() > plugin.getWorlds().getInt("WORLDS." + worldName + ".VOID-POSITION", plugin.getWorlds().getInt("WORLDS." + worldName + ".VOID-POSITION"))) {
            return;
        }
        if (plugin.getWorlds().getBoolean("WORLDS." + player.getWorld().getName() + ".TP-WHEN-FALLING")) {
            if (plugin.getWorlds().contains("WORLDS." + player.getWorld().getName() + ".VOID-POSITION")) {
                setEffect("REMOVE", player);
                getTeleportation(player);
                getJump(player);
                getLightningEffect(player);
                getParticles(player);
                sendCommands(player);
                getNull(player);
            }
        }
    }

    private void getNull(Player player) {
        VOManager.isNull(player);
    }

    private void getTeleportation(Player player) {
        if (!plugin.getIgnoreTeleportation().contains(player.getUniqueId())) {
            VOManager.getTeleportation(player);
        }
    }

    private void getJump(Player player) {
        if (!plugin.getIgnoreJumping().contains(player.getUniqueId())) {
            plugin.getDamage().add(player.getUniqueId());
            VOManager.getJumpEffects(player);
        }
    }

    private void getLightningEffect(Player player) {
        if (!plugin.getIgnoreLightning().contains(player.getUniqueId())) {
            VOManager.getLightningEffect(player);
        }
    }

    private void getParticles(Player player) {
        if (!plugin.getIgnoreParticles().contains(player.getUniqueId())) {
            VOManager.getParticles(player);
        }
    }

    private void setEffect(String type, Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.BACKGROUND_EFFECT.ENABLED")) {
            if (type.contains("ADD")) {
                VOManager.sendBackGround("ADD", player);
            } else if (type.contains("REMOVE")) {
                VOManager.sendBackGround("REMOVE", player);
            }
        }
    }

    private void sendCommands(Player player) {
        final String worldName = player.getWorld().getName();
        if (Objects.equals(plugin.getWorlds().getString("WORLDS." + worldName + ".COMMANDS.TYPE"), "CONSOLE")) {
            for (String command : plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST")) {
                command = VOManager.replacePlaceholder(command, "{Player}", player.getName());
                command = VOManager.replacePlaceholder(command, "{Uuid}", player.getUniqueId().toString());
                command = VOManager.replacePlaceholder(command, "{Address}", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), addColor.setColors(command));
            }
        } else if (Objects.equals(plugin.getWorlds().getString("WORLDS." + worldName + ".COMMANDS.TYPE"), "PLAYER")) {
            for (String command : plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST")) {
                command = VOManager.replacePlaceholder(command, "{Player}", player.getName());
                command = VOManager.replacePlaceholder(command, "{Uuid}", player.getUniqueId().toString());
                command = VOManager.replacePlaceholder(command, "{Address}", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
                Bukkit.dispatchCommand(player, addColor.setColors(command));
            }
        }
    }
}