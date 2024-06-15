package ruben_artz.world.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import ruben_artz.world.features.addColor;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.ProjectUtils;

public class Player {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public static final String title = addColor.setColors(plugin.getBoolean().getString("MAIN.TITLE"));

    public static void getInventory(org.bukkit.entity.Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, title);
        //                      GLASS
        if (plugin.getBoolean().getBoolean("MAIN.FILL_EMPTY")) {
            for (int o=45;o<=53;o++) {
                ProjectUtils.setGlass(o, inv);
            }
        }
        if (Launcher.getCache().getVerify(player.getUniqueId(), "TELEPORT")) {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.TELEPORTATION.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_ENABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_ENABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_ENABLED"));
        } else {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.TELEPORTATION.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_DISABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_DISABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_DISABLED"));
        }
        if (Launcher.getCache().getVerify(player.getUniqueId(), "JUMP")) {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.JUMP.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_ENABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_ENABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_ENABLED"));
        } else {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.JUMP.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_DISABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_DISABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_DISABLED"));
        }
        if (Launcher.getCache().getVerify(player.getUniqueId(), "LIGHTNING")) {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.LIGHTNING.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_ENABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_ENABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_ENABLED"));
        } else {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.LIGHTNING.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_DISABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_DISABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_DISABLED"));
        }
        if (Launcher.getCache().getVerify(player.getUniqueId(), "PARTICLES")) {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.PARTICLES.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_ENABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_ENABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_ENABLED"));
        } else {
            ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.BOOLEAN.SLOTS.PARTICLES.BOOLEAN"), inv, plugin.getBoolean().getString("MAIN.BOOLEAN.MATERIAL_DISABLED", "BEDROCK"), plugin.getBoolean().getString("MAIN.BOOLEAN.NAME_DISABLED"), plugin.getBoolean().getStringList("MAIN.BOOLEAN.LORE_DISABLED"));
        }
        //                                              HEADS
        ProjectUtils.setSkullTexture(plugin.getBoolean().getInt("MAIN.TELEPORTATION.SLOT"), inv, plugin.getBoolean().getString("MAIN.TELEPORTATION.NAME"), plugin.getBoolean().getStringList("MAIN.TELEPORTATION.LORE"), plugin.getBoolean().getString("MAIN.TELEPORTATION.VALUE"));
        ProjectUtils.setSkullTexture(plugin.getBoolean().getInt("MAIN.JUMP.SLOT"), inv, plugin.getBoolean().getString("MAIN.JUMP.NAME"), plugin.getBoolean().getStringList("MAIN.JUMP.LORE"), plugin.getBoolean().getString("MAIN.JUMP.VALUE"));
        ProjectUtils.setSkullTexture(plugin.getBoolean().getInt("MAIN.LIGHTNING.SLOT"), inv, plugin.getBoolean().getString("MAIN.LIGHTNING.NAME"), plugin.getBoolean().getStringList("MAIN.LIGHTNING.LORE"), plugin.getBoolean().getString("MAIN.LIGHTNING.VALUE"));
        ProjectUtils.setSkullTexture(plugin.getBoolean().getInt("MAIN.PARTICLES.SLOT"), inv, plugin.getBoolean().getString("MAIN.PARTICLES.NAME"), plugin.getBoolean().getStringList("MAIN.PARTICLES.LORE"), plugin.getBoolean().getString("MAIN.PARTICLES.VALUE"));
        //                                              ITEMS
        ProjectUtils.setItem(plugin.getBoolean().getInt("MAIN.CLOSE.SLOT"), inv, plugin.getBoolean().getString("MAIN.CLOSE.MATERIAL", "BEDROCK"), plugin.getBoolean().getString("MAIN.CLOSE.NAME"), plugin.getBoolean().getStringList("MAIN.CLOSE.LORE"));
        player.openInventory(inv);
    }
}
