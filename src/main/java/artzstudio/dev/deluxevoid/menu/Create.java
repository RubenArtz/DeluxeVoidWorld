package artzstudio.dev.deluxevoid.menu;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.data.world.WorldManager;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Objects;

public class Create {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void openInventory(Player player) {
        YamlDocument config = plugin.getInventoryCrating();
        String title = addColor.setColors(config.getString("MAIN.TITLE", "Create World"));

        List<WorldManager> messages = plugin.getMessage();
        if (messages.isEmpty()) return;

        String worldName = messages.get(0).getName();

        Inventory inventory = Bukkit.createInventory(null, 36, title);

        for (int i = 27; i < 36; i++) {
            UtilityFunctions.setGlass(i, inventory);
        }

        setupWorldItem(inventory, config, "MAIN.NORMAL", worldName);
        setupWorldItem(inventory, config, "MAIN.NETHER", worldName);
        setupWorldItem(inventory, config, "MAIN.THE_END", worldName);

        setupStaticItem(inventory, config, "MAIN.CLOSE");
        setupStaticItem(inventory, config, "MAIN.RETURN");

        player.openInventory(inventory);
    }

    private static void setupWorldItem(Inventory inv, YamlDocument config, String path, String worldName) {
        String name = Objects.requireNonNull(config.getString(path + ".NAME")).replace("{World}", worldName);

        UtilityFunctions.setItem(
                config.getInt(path + ".SLOT"),
                inv,
                config.getString(path + ".MATERIAL", "BEDROCK"),
                name,
                config.getStringList(path + ".LORE")
        );
    }

    private static void setupStaticItem(Inventory inv, YamlDocument config, String path) {
        UtilityFunctions.setItem(
                config.getInt(path + ".SLOT"),
                inv,
                config.getString(path + ".MATERIAL", "BEDROCK"),
                config.getString(path + ".NAME"),
                config.getStringList(path + ".LORE")
        );
    }
}