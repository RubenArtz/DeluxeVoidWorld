package ruben_artz.world.events.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ruben_artz.world.main.VOMain;

public class VOLeave implements Listener {
    public final VOMain plugin = VOMain.getPlugin(VOMain.class);

    @EventHandler
    public void onPlayerQuitEffects(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (int i = 0; i < 100; i++) {
            plugin.damage.remove(event.getPlayer().getUniqueId());
            plugin.IgnoreJumping.remove(player.getUniqueId());
            plugin.IgnoreTeleportation.remove(player.getUniqueId());
            plugin.IgnoreLightning.remove(player.getUniqueId());
            plugin.IgnoreParticles.remove(player.getUniqueId());
            event.getPlayer().setAllowFlight(false);
        }
    }
}
