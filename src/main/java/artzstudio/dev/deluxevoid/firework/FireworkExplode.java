/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.firework;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import com.github.Anon8281.universalScheduler.UniversalRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;

public class FireworkExplode implements Listener {

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Entity firework = event.getEntity();
        if (FireworkManager.getFireworks().contains(firework)) {

            new UniversalRunnable() {
                @Override
                public void run() {
                    FireworkManager.removeFirework(firework);
                }
            }.runTaskLater(DeluxeVoidWorld.getInstance(), 20L);
        }
    }
}