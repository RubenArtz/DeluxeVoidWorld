package ruben_artz.world.firework;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ruben_artz.world.DeluxeVoidWorld;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "RedundantCollectionOperation"})
public class FireworkManager {
    private static final ArrayList<Entity> fireworks = new ArrayList<>();

    public static ArrayList<Entity> getFireworks() {
        return FireworkManager.fireworks;
    }

    public static void addFirework(final Entity firework) {
        FireworkManager.fireworks.add(firework);
    }

    public static void removeFirework(final Entity firework) {
        if (FireworkManager.fireworks.contains(firework)) {
            FireworkManager.fireworks.remove(firework);
        }
    }

    public static void launchFirework(final Location location, final ArrayList<Color> colors) {
        String types = DeluxeVoidWorld.getInstance().getConfig().getString("ON_VOID_TP.SETTINGS.FIREWORK.TYPE");
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.valueOf("FIREWORK"));
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffects(FireworkEffect.builder().with(FireworkEffect.Type.valueOf(types)).withColor(colors).trail(false).flicker(false).build());
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
        FireworkManager.addFirework(firework);
        new BukkitRunnable() {
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(DeluxeVoidWorld.getInstance(), 2L);
    }
}