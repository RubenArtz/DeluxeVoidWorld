package ruben_artz.world.firework;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ruben_artz.world.DeluxeVoidWorld;

public class FireworkExplode implements Listener {
    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Entity firework = event.getEntity();
        if (FireworkManager.getFireworks().contains(firework)) {
            new BukkitRunnable() {
                public void run() {
                    FireworkManager.removeFirework(firework);
                }
            }.runTaskLater(DeluxeVoidWorld.getInstance(), 5L);
        }
    }
}