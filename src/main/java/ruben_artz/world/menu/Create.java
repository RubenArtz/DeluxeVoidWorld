package ruben_artz.world.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ruben_artz.world.events.chat.VOString;
import ruben_artz.world.features.addColor;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Objects;

public class Create {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public static String title = addColor.setColors(Objects.requireNonNull(plugin.getInventoryCrating().getString("MAIN.TITLE")));

    /*
     * Open inventory for create worlds
     */
    public static void openInventory(Player player) {
        ArrayList<VOString> messages = plugin.getMessage();
        VOString message = messages.get(0);

        Inventory inventory = Bukkit.createInventory(null, 36, title);
        /*
         * Adding crystals
         */
        for (int glass = 27; glass <= 35; glass++) {
            ProjectUtils.setGlass(glass, inventory);
        }
        /*
         * Adding items
         */
        ProjectUtils.setItem(plugin.getInventoryCrating().getInt("MAIN.NORMAL.SLOT"), inventory, plugin.getInventoryCrating().getString("MAIN.NORMAL.MATERIAL", "BEDROCK"), Objects.requireNonNull(plugin.getInventoryCrating().getString("MAIN.NORMAL.NAME")).replace("{World}", message.getName()), plugin.getInventoryCrating().getStringList("MAIN.NORMAL.LORE"));
        ProjectUtils.setItem(plugin.getInventoryCrating().getInt("MAIN.NETHER.SLOT"), inventory, plugin.getInventoryCrating().getString("MAIN.NETHER.MATERIAL", "BEDROCK"), Objects.requireNonNull(plugin.getInventoryCrating().getString("MAIN.NETHER.NAME")).replace("{World}", message.getName()), plugin.getInventoryCrating().getStringList("MAIN.NETHER.LORE"));
        ProjectUtils.setItem(plugin.getInventoryCrating().getInt("MAIN.THE_END.SLOT"), inventory, plugin.getInventoryCrating().getString("MAIN.THE_END.MATERIAL", "BEDROCK"), Objects.requireNonNull(plugin.getInventoryCrating().getString("MAIN.THE_END.NAME")).replace("{World}", message.getName()), plugin.getInventoryCrating().getStringList("MAIN.THE_END.LORE"));
        ProjectUtils.setItem(plugin.getInventoryCrating().getInt("MAIN.CLOSE.SLOT"), inventory, plugin.getInventoryCrating().getString("MAIN.CLOSE.MATERIAL", "BEDROCK"), plugin.getInventoryCrating().getString("MAIN.CLOSE.NAME"), plugin.getInventoryCrating().getStringList("MAIN.CLOSE.LORE"));
        ProjectUtils.setItem(plugin.getInventoryCrating().getInt("MAIN.RETURN.SLOT"), inventory, plugin.getInventoryCrating().getString("MAIN.RETURN.MATERIAL", "BEDROCK"), plugin.getInventoryCrating().getString("MAIN.RETURN.NAME"), plugin.getInventoryCrating().getStringList("MAIN.RETURN.LORE"));

        player.openInventory(inventory);
    }
}
