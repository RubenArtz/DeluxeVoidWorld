package ruben_artz.world.menu;

import com.cryptomorin.xseries.XMaterial;
import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.menu.utils.playerPageInfo;
import ruben_artz.world.utils.ProjectUtils;
import ruben_artz.world.utils.addColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home {
    public static final List<ItemStack> itemStacks = new ArrayList<>();
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public static final String title = addColor.setColors(plugin.getMenuHome().getString("MAIN.TITLE"));
    public static MyScheduledTask task;
    @Getter
    public static Inventory inventory;

    /*
    This method creates the inventory
     */
    public static void getInventory(Player player, int currentPage) {
        ProjectUtils.runTask(() -> {
            inventory = Bukkit.createInventory(null, 54, title);
           /*
           This method updates the item, lore and name
           */
            task = ProjectUtils.runTaskTimer(20, () -> {
                /*
                 * Adding crystals
                 */
                for (int glass = 45; glass <= 53; glass++) {
                    ProjectUtils.setGlass(glass, inventory);
                }
                /*
                 * Adding items
                 */
                ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.ADD_WORLD.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.ADD_WORLD.MATERIAL", "BEDROCK"), plugin.getMenuHome().getString("MAIN.ADD_WORLD.NAME"), plugin.getMenuHome().getStringList("MAIN.ADD_WORLD.LORE"));
                ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.CLOSE.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.CLOSE.MATERIAL", "BEDROCK"), plugin.getMenuHome().getString("MAIN.CLOSE.NAME"), plugin.getMenuHome().getStringList("MAIN.CLOSE.LORE"));
                ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.RELOAD.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.RELOAD.MATERIAL", "BEDROCK"), plugin.getMenuHome().getString("MAIN.RELOAD.NAME"), plugin.getMenuHome().getStringList("MAIN.RELOAD.LORE"));
                ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.CREATE_WORLD.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.CREATE_WORLD.MATERIAL", "BEDROCK"), plugin.getMenuHome().getString("MAIN.CREATE_WORLD.NAME"), plugin.getMenuHome().getStringList("MAIN.CREATE_WORLD.LORE"));

                /*
                 * Add more pages
                 */
                int totalPages = ProjectUtils.getInventoryPages();
                int slot = 0;
                setBooks(player);
                for (int i = 45 * (currentPage - 1); i < ProjectUtils.getWorldPathSize(); i++) {
                    inventory.setItem(slot++, itemStacks.get(i));
                    if (slot > 44) {
                        break;
                    }
                }
                /*
                Next page
                */
                if (totalPages > currentPage) {
                    List<String> lore = plugin.getMenuHome().getStringList("MAIN.NEXT.LORE");
                    lore.replaceAll(s -> s
                            .replace("{Current Page}", String.valueOf(currentPage))
                            .replace("{Max Pages}", String.valueOf(ProjectUtils.getInventoryPages())));
                    ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.NEXT.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.NEXT.MATERIAL"), plugin.getMenuHome().getString("MAIN.NEXT.NAME"), lore, currentPage + 1);
                }
                /*
                Return to page
                */
                if (currentPage > 1) {
                    List<String> lore = plugin.getMenuHome().getStringList("MAIN.RETURN.LORE");
                    lore.replaceAll(s -> s
                            .replace("{Current Page}", String.valueOf(currentPage))
                            .replace("{Max Pages}", String.valueOf(ProjectUtils.getInventoryPages())));
                    ProjectUtils.setItem(plugin.getMenuHome().getInt("MAIN.RETURN.SLOT"), inventory, plugin.getMenuHome().getString("MAIN.RETURN.MATERIAL"), plugin.getMenuHome().getString("MAIN.RETURN.NAME"), lore, currentPage - 1);
                }
            });
            player.openInventory(inventory);
            plugin.addInventory(new playerPageInfo(player, currentPage));
        });
    }

    /*
    Add items in book form
     */
    public static void setBooks(Player player) {
        if (!itemStacks.isEmpty()) itemStacks.clear();
        for (String key : Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false)) {
            final ItemStack itemStack = XMaterial.valueOf(plugin.getWorlds().getString("WORLDS." + key + ".MATERIAL")).parseItem();
            final ItemMeta itemMeta = itemStack != null ? itemStack.getItemMeta() : null;
            final String[] name = Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + key + ".SPAWN")).split(",");
            final World world = Bukkit.getWorld(name[0]);
            final String itemName;

            if (world == null) {
                itemName = "???";
            } else {
                if (world.getPlayers().isEmpty()) {
                    itemName = "0";
                } else {
                    itemName = ProjectUtils.addCommas(world.getPlayers().size());
                }
            }

            if (itemMeta != null) itemMeta.setDisplayName(
                    addColor.setColors(ProjectUtils.setPlaceholders(plugin.getWorlds().getString("WORLDS." + key + ".WORLD")) + " &7| &e( " + itemName + " Player's )"));

            final List<String> loreList = plugin.getMenuHome().getStringList("MAIN.ALPHANUMERIC_ITEM.LORE");
            loreList.replaceAll((s) -> {
                /*
                Verify teleportation
                 */
                if (Objects.equals(plugin.getWorlds().getString("WORLDS." + key + ".TP-WHEN-FALLING"), "true")) {
                    s = ProjectUtils.replacePlaceholder(s, "{Status}", plugin.getConfig().getString("ADMIN-CONFIG.PLACEHOLDER.ENABLED"));
                } else {
                    s = ProjectUtils.replacePlaceholder(s, "{Status}", plugin.getConfig().getString("ADMIN-CONFIG.PLACEHOLDER.DISABLED"));
                }

                /*
                It is in the world
                 */
                if (player.getWorld().getName().equalsIgnoreCase(name[0])) {
                    s = ProjectUtils.replacePlaceholder(s, "{Current World}", plugin.getFileTranslations().getString("MESSAGE_PLACEHOLDER_WORLD_YES"));
                } else {
                    s = ProjectUtils.replacePlaceholder(s, "{Current World}", plugin.getFileTranslations().getString("MESSAGE_PLACEHOLDER_WORLD_NO"));
                }

                /*
                Always daytime?
                 */
                if (Objects.equals(plugin.getWorlds().getString("WORLDS." + key + ".ALWAYS-DAY"), "true")) {
                    s = ProjectUtils.replacePlaceholder(s, "{Status Day}", plugin.getConfig().getString("ADMIN-CONFIG.PLACEHOLDER.ENABLED"));
                } else {
                    s = ProjectUtils.replacePlaceholder(s, "{Status Day}", plugin.getConfig().getString("ADMIN-CONFIG.PLACEHOLDER.DISABLED"));
                }

                s = ProjectUtils.replacePlaceholder(s, "{Position}", plugin.getWorlds().getString("WORLDS." + key + ".VOID-POSITION"));
                s = ProjectUtils.replacePlaceholder(s, "{X}", name[1]);
                s = ProjectUtils.replacePlaceholder(s, "{Y}", name[2]);
                s = ProjectUtils.replacePlaceholder(s, "{Z}", name[3]);
                s = ProjectUtils.replacePlaceholder(s, "{Yaw}", name[4]);
                s = ProjectUtils.replacePlaceholder(s, "{Pitch}", name[5]);
                return addColor.setColors(ProjectUtils.setPlaceholders(s));
            });

            if (itemMeta != null) itemMeta.setLore(loreList);

            if (itemStack != null) itemStack.setItemMeta(itemMeta);

            itemStacks.add(itemStack);
        }
    }
}