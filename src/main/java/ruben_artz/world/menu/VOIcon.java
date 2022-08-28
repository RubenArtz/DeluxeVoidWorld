package ruben_artz.world.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ruben_artz.world.features.addColor;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;

import java.util.Objects;

public class VOIcon {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);
    public static final String title = addColor.setColors(plugin.getIcons().getString("MAIN.TITLE"));

    public static void getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, title.replace("{Name World}", player.getWorld().getName()));
        //                      GLASS
        for (int i = 45; i <= 53; i++) {
            VOManager.setGlass(i, inv);
        }

        VOManager.setItem(plugin.getIcons().getInt("MAIN.CLOSE.SLOT"), inv, plugin.getIcons().getString("MAIN.CLOSE.MATERIAL", "BEDROCK"), plugin.getIcons().getString("MAIN.CLOSE.NAME"), plugin.getIcons().getStringList("MAIN.CLOSE.LORE"));
        VOManager.setItem(plugin.getIcons().getInt("MAIN.RETURN.SLOT"), inv, plugin.getIcons().getString("MAIN.RETURN.MATERIAL", "BEDROCK"), plugin.getIcons().getString("MAIN.RETURN.NAME"), plugin.getIcons().getStringList("MAIN.RETURN.LORE"));

        int slot = 0;
        for (String key : Objects.requireNonNull(plugin.getIcons().getConfigurationSection("MATERIALS")).getKeys(false)) {
            VOManager.setItem(slot, inv, plugin.getIcons().getString("MATERIALS." + key + ".ITEM"), "&b"+ plugin.getIcons().getString("MATERIALS." + key + ".ITEM"), plugin.getFileTranslations().getStringList("MESSAGE_LORE_ICONS"));
            slot++;
            if (slot == 45) {
                break;
            }
        }

        player.openInventory(inv);
    }
}
