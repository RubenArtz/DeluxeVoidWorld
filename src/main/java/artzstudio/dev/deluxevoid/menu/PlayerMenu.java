package artzstudio.dev.deluxevoid.menu;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.data.player.PlayerData;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PlayerMenu {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void getInventory(org.bukkit.entity.Player player) {
        YamlDocument config = plugin.getBoolean();
        String title = addColor.setColors(config.getString("MAIN.TITLE", "Option Selector"));

        Inventory inv = Bukkit.createInventory(null, 54, title);

        PlayerData data = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (config.getBoolean("MAIN.FILL_EMPTY")) {
            for (int i = 45; i <= 53; i++) {
                UtilityFunctions.setGlass(i, inv);
            }
        }

        setupBooleanItem(inv, "TELEPORTATION", data.isTeleportEnabled(), config);
        setupBooleanItem(inv, "JUMP", data.isJumpEnabled(), config);
        setupBooleanItem(inv, "LIGHTNING", data.isLightningEnabled(), config);
        setupBooleanItem(inv, "PARTICLES", data.isParticlesEnabled(), config);

        setupSkullItem(inv, "TELEPORTATION", config);
        setupSkullItem(inv, "JUMP", config);
        setupSkullItem(inv, "LIGHTNING", config);
        setupSkullItem(inv, "PARTICLES", config);

        UtilityFunctions.setItem(
                config.getInt("MAIN.CLOSE.SLOT"),
                inv,
                config.getString("MAIN.CLOSE.MATERIAL", "BEDROCK"),
                config.getString("MAIN.CLOSE.NAME"),
                config.getStringList("MAIN.CLOSE.LORE")
        );

        player.openInventory(inv);
    }

    private static void setupBooleanItem(Inventory inv, String key, boolean isEnabled, YamlDocument config) {
        String basePath = "MAIN.BOOLEAN.";
        int slot = config.getInt(basePath + "SLOTS." + key + ".BOOLEAN");

        String suffix = isEnabled ? "ENABLED" : "DISABLED";

        UtilityFunctions.setItem(
                slot,
                inv,
                config.getString(basePath + "MATERIAL_" + suffix, "BEDROCK"),
                config.getString(basePath + "NAME_" + suffix),
                config.getStringList(basePath + "LORE_" + suffix)
        );
    }

    private static void setupSkullItem(Inventory inv, String key, YamlDocument config) {
        String path = "MAIN." + key;
        UtilityFunctions.setSkullTexture(
                config.getInt(path + ".SLOT"),
                inv,
                config.getString(path + ".NAME"),
                config.getStringList(path + ".LORE"),
                config.getString(path + ".VALUE")
        );
    }
}