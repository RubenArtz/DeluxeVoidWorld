package ruben_artz.world.firework;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireworkDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        for (Entity entities : event.getEntity().getNearbyEntities(5.0, 5.0, 5.0)) {
            if ((entities.getType() == EntityType.FIREWORK) && (FireworkManager.getFireworks().contains(entities))) {
                event.setCancelled(true);
            }
        }
    }
}
