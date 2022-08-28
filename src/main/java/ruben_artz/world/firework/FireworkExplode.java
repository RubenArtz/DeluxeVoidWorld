package ruben_artz.world.firework;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ruben_artz.world.main.VOMain;

public class FireworkExplode implements Listener {
    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Entity firework = event.getEntity();
        if (FireworkManager.getFireworks().contains(firework)) {
            new BukkitRunnable() {
                public void run() {
                    FireworkManager.removeFirework(firework);
                }
            }.runTaskLater(VOMain.getInstance(), 5L);
        }
    }
}