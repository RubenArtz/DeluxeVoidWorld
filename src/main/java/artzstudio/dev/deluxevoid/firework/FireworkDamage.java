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

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireworkDamage implements Listener {
    private static final EntityType FIREWORK_TYPE;

    static {
        EntityType tempType;
        try {
            tempType = EntityType.valueOf("FIREWORK_ROCKET");
        } catch (IllegalArgumentException e) {
            tempType = EntityType.valueOf("FIREWORK");
        }
        FIREWORK_TYPE = tempType;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        for (Entity entities : event.getEntity().getNearbyEntities(5.0, 5.0, 5.0)) {
            if (entities.getType() == FIREWORK_TYPE && FireworkManager.getFireworks().contains(entities)) {
                event.setCancelled(true);
            }
        }
    }
}