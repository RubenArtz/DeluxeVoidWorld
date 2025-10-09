package ruben_artz.world.events.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ruben_artz.world.events.world.antibot.preventAttacks;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.utils.CrossPlatformUtils;

public class VOJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void verifyPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CrossPlatformUtils.runTaskAsynchronously(() -> {
            /*
             * Add data if the player does not exist!
             */
            try {
                if (Launcher.getCache().ifNotExists(player.getUniqueId())) {
                    if (preventAttacks.isAttacking()) return;
                    Launcher.getCache().addInformation(player.getUniqueId());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Launcher.getCache().updatePlayer(player);
        });
    }
}
