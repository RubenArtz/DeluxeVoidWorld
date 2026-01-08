package artzstudio.dev.deluxevoid.events.world;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageEvent implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onPlayerFall(org.bukkit.event.entity.EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getCause() != DamageCause.FALL) return;

        if (plugin.getSessionManager().isDamageImmune(event.getEntity().getUniqueId())) {
            event.setCancelled(true);

            UtilityFunctions.runTaskLater(200, () ->
                    plugin.getSessionManager().removeDamageImmunity(event.getEntity().getUniqueId())
            );
        }
    }
}