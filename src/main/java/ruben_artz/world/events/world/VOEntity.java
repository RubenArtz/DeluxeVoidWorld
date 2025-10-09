package ruben_artz.world.events.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.CrossPlatformUtils;

public class VOEntity implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) && !(event.getEntity() instanceof org.bukkit.entity.Pig) && !(event.getEntity() instanceof org.bukkit.entity.Horse)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if (plugin.getDamage().contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
            CrossPlatformUtils.runTaskLater(200, () -> plugin.getDamage().clear());
        }
    }
}
