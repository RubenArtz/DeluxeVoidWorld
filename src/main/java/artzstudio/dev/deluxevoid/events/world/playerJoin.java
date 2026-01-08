package artzstudio.dev.deluxevoid.events.world;

import artzstudio.dev.deluxevoid.events.world.antibot.preventAttacks;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void verifyPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UtilityFunctions.runTaskAsynchronously(() -> {
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
