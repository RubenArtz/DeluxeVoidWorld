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
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions"})
public class FireworkManager {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    private static final ArrayList<Entity> fireworks = new ArrayList<>();
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

    public static ArrayList<Entity> getFireworks() {
        return FireworkManager.fireworks;
    }

    public static void addFirework(final Entity firework) {
        FireworkManager.fireworks.add(firework);
    }

    public static void removeFirework(final Entity firework) {
        FireworkManager.fireworks.remove(firework);
    }

    public static void launchFirework(final Location location, final ArrayList<Color> colors) {
        String typeConfig = plugin.getConfigYaml().getString("ON_VOID_TP.SETTINGS.FIREWORK.TYPE");

        Firework firework = (Firework) location.getWorld().spawnEntity(location, FIREWORK_TYPE);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffects(FireworkEffect.builder()
                .with(FireworkEffect.Type.valueOf(typeConfig))
                .withColor(colors)
                .trail(false)
                .flicker(false)
                .build());
        fireworkMeta.setPower(0);

        firework.setFireworkMeta(fireworkMeta);
        FireworkManager.addFirework(firework);

        firework.detonate();
    }
}