package ruben_artz.world.events.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ruben_artz.world.DeluxeVoidWorld;

public class VOLeave implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onPlayerQuitEffects(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (int i = 0; i < 100; i++) {
            plugin.getDamage().remove(event.getPlayer().getUniqueId());
            plugin.getIgnoreJumping().remove(player.getUniqueId());
            plugin.getIgnoreTeleportation().remove(player.getUniqueId());
            plugin.getIgnoreLightning().remove(player.getUniqueId());
            plugin.getIgnoreParticles().remove(player.getUniqueId());
            event.getPlayer().setAllowFlight(false);
        }
    }
}
