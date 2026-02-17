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

package artzstudio.dev.deluxevoid.features;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.Particles;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class sendParticles {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void sendParticle(Player player) {
        if (UtilityFunctions.isVersion_1_10_To_1_21()) {
            if (plugin.getConfigYaml().getBoolean("ON_VOID_TP.SETTINGS.PARTICLES.ENABLED")) {

                for (String particles : plugin.getConfigYaml().getStringList("ON_VOID_TP.SETTINGS.PARTICLES.LIST")) {
                    String[] particle = particles.split(";");
                    player.spawnParticle(Particle.valueOf(particle[0]), player.getLocation(), Integer.parseInt(particle[1]), Float.parseFloat(particle[2]), Float.parseFloat(particle[3]), Float.parseFloat(particle[4]), Float.parseFloat(particle[5]), null);
                }

                UtilityFunctions.runTaskLater(20L, () -> UtilityFunctions.runTaskTimerTick(() -> {
                    Particle xParticle = XParticle.DRAGON_BREATH.get();

                    if (xParticle != null) {
                        try {
                            Particles.circle(
                                    3,
                                    100,
                                    ParticleDisplay.of(XParticle.of(xParticle))
                                            .withLocation(player.getLocation())
                            );
                        } catch (Exception ignored) {
                        }
                    }
                }));
            }
        }
    }
}