package ruben_artz.world.features;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import ruben_artz.world.main.DeluxeVoidWorld;
import ruben_artz.world.world.VOManager;

public class sendParticles {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void sendParticle(Player player) {
        if (VOManager.isVersion_1_10_To_1_19()) {
            if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.PARTICLES.ENABLED")) {
                for (String particles : plugin.getConfig().getStringList("ON_VOID_TP.SETTINGS.PARTICLES.LIST")) {
                    String[] particle = particles.split(";");
                    player.spawnParticle(Particle.valueOf(particle[0]), player.getLocation(), Integer.parseInt(particle[1]), Float.parseFloat(particle[2]), Float.parseFloat(particle[3]), Float.parseFloat(particle[4]), Float.parseFloat(particle[5]), null);
                }
            }
        }
    }
}
