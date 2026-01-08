package artzstudio.dev.deluxevoid.menu.icon;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.menu.home.WorldManager;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.IOException;
import java.util.*;

public class IconListener implements Listener {

    private static final Map<UUID, Integer> pageSession = new HashMap<>();
    private final DeluxeVoidWorld plugin;
    private final IconMenu iconMenu;
    private final IconManager iconManager;
    private final Set<UUID> isSwitching = new HashSet<>();

    public IconListener(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.iconMenu = new IconMenu(plugin);
        this.iconManager = new IconManager();
    }

    public void openInventory(Player player) {
        String targetWorld = new WorldManager(plugin).getEditingWorld(player.getUniqueId());
        if (targetWorld == null) targetWorld = player.getWorld().getName();

        pageSession.put(player.getUniqueId(), 0);
        iconMenu.open(player, 0, targetWorld);
    }

    private void changePage(Player player, int page) {
        String targetWorld = new WorldManager(plugin).getEditingWorld(player.getUniqueId());
        if (targetWorld == null) targetWorld = player.getWorld().getName();

        isSwitching.add(player.getUniqueId());
        pageSession.put(player.getUniqueId(), page);

        iconMenu.open(player, page, targetWorld);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        String targetWorld = new WorldManager(plugin).getEditingWorld(player.getUniqueId());
        if (targetWorld == null) targetWorld = player.getWorld().getName();

        YamlDocument icons = plugin.getIcons();
        String rawTitle = icons.getString("MAIN.TITLE", "&8Icons Selector");

        String expectedTitle = addColor.setColors(rawTitle.replace("{Name World}", targetWorld));

        if (!ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(expectedTitle))) return;
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        event.setCancelled(true);
        int slot = event.getSlot();
        int currentPage = pageSession.getOrDefault(player.getUniqueId(), 0);
        List<String> allMaterials = iconManager.getSortedMaterials();

        int prevSlot = icons.getInt("MAIN.PREVIOUS_PAGE.SLOT", 46);
        int nextSlot = icons.getInt("MAIN.NEXT_PAGE.SLOT", 51);

        if (slot == prevSlot && currentPage > 0) {
            UtilityFunctions.executeSound("UI_BUTTON_CLICK", player);
            changePage(player, currentPage - 1);
            return;
        }
        if (slot == nextSlot && (currentPage + 1) * IconMenu.PAGE_SIZE < allMaterials.size()) {
            UtilityFunctions.executeSound("UI_BUTTON_CLICK", player);
            changePage(player, currentPage + 1);
            return;
        }

        int closeSlot = icons.getInt("MAIN.CLOSE.SLOT", -1);
        int returnSlot = icons.getInt("MAIN.RETURN.SLOT", -1);

        if (slot == closeSlot) {
            player.closeInventory();
            return;
        } else if (slot == returnSlot) {
            Launcher.getInstance().getHomeListener().openRestore(player);
            return;
        }

        if (slot < IconMenu.PAGE_SIZE) {
            int realIndex = (currentPage * IconMenu.PAGE_SIZE) + slot;
            if (realIndex >= allMaterials.size()) return;

            String selectedXMaterial = allMaterials.get(realIndex);

            YamlDocument worlds = plugin.getWorlds();
            worlds.set("WORLDS." + targetWorld + ".MATERIAL", selectedXMaterial);

            try {
                worlds.save();
                plugin.initiate();
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }

            UtilityFunctions.executeSound("ENTITY_EXPERIENCE_ORB_PICKUP", player);
            Launcher.getInstance().getHomeListener().openRestore(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        String targetWorld = new WorldManager(plugin).getEditingWorld(player.getUniqueId());
        if (targetWorld == null) targetWorld = player.getWorld().getName();

        String rawTitle = plugin.getIcons().getString("MAIN.TITLE", "&8Icons Selector");
        String expectedTitle = addColor.setColors(rawTitle.replace("{Name World}", targetWorld));

        if (ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(expectedTitle))) {
            UUID uuid = player.getUniqueId();
            if (isSwitching.contains(uuid)) {
                isSwitching.remove(uuid);
                return;
            }
            pageSession.remove(uuid);
        }
    }
}