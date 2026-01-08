package artzstudio.dev.deluxevoid.menu.icon;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.utils.UtilityFunctions;
import artzstudio.dev.deluxevoid.utils.addColor;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class IconMenu {

    public static final int PAGE_SIZE = 45;
    private final DeluxeVoidWorld plugin;
    private final IconManager iconManager;

    public IconMenu(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.iconManager = new IconManager();
    }

    public void open(Player player, int page, String worldKey) {
        String rawTitle = plugin.getIcons().getString("MAIN.TITLE");

        String title = addColor.setColors(Objects.requireNonNull(rawTitle)
                .replace("{Name World}", worldKey));

        Inventory inv = Bukkit.createInventory(null, 54, title);

        List<String> allMaterials = iconManager.getSortedMaterials();

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allMaterials.size());

        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String materialName = allMaterials.get(i);

            Optional<XMaterial> xMat = XMaterial.matchXMaterial(materialName);

            if (xMat.isPresent()) {
                String displayName = "&b" + materialName;

                List<String> lore = plugin.getFileTranslations().getStringList("MESSAGE_LORE_ICONS");

                UtilityFunctions.setItem(slot, inv, materialName, displayName, lore);
                slot++;
            }
        }

        fillGlass(inv);
        setupStaticButtons(inv);
        setupNavigationButtons(inv, page, allMaterials.size(), endIndex);

        player.openInventory(inv);
    }

    private void fillGlass(Inventory inv) {
        for (int i = 45; i <= 53; i++) {
            UtilityFunctions.setGlass(i, inv);
        }
    }

    private void setupStaticButtons(Inventory inv) {
        UtilityFunctions.setItem(plugin.getIcons().getInt("MAIN.CLOSE.SLOT"), inv, plugin.getIcons().getString("MAIN.CLOSE.MATERIAL", "BEDROCK"), plugin.getIcons().getString("MAIN.CLOSE.NAME"), plugin.getIcons().getStringList("MAIN.CLOSE.LORE"));
        UtilityFunctions.setItem(plugin.getIcons().getInt("MAIN.RETURN.SLOT"), inv, plugin.getIcons().getString("MAIN.RETURN.MATERIAL", "BEDROCK"), plugin.getIcons().getString("MAIN.RETURN.NAME"), plugin.getIcons().getStringList("MAIN.RETURN.LORE"));
    }

    private void setupNavigationButtons(Inventory inv, int page, int totalItems, int endIndex) {
        if (page > 0) {
            int slot = plugin.getIcons().getInt("MAIN.PREVIOUS_PAGE.SLOT", 46);
            List<String> lore = new ArrayList<>();
            for (String s : plugin.getIcons().getStringList("MAIN.PREVIOUS_PAGE.LORE"))
                lore.add(s.replace("{page}", String.valueOf(page)));
            UtilityFunctions.setItem(slot, inv, plugin.getIcons().getString("MAIN.PREVIOUS_PAGE.MATERIAL", "ARROW"), plugin.getIcons().getString("MAIN.PREVIOUS_PAGE.NAME"), lore);
        }
        if (endIndex < totalItems) {
            int slot = plugin.getIcons().getInt("MAIN.NEXT_PAGE.SLOT", 51);
            List<String> lore = new ArrayList<>();
            for (String s : plugin.getIcons().getStringList("MAIN.NEXT_PAGE.LORE"))
                lore.add(s.replace("{page}", String.valueOf(page + 2)));
            UtilityFunctions.setItem(slot, inv, plugin.getIcons().getString("MAIN.NEXT_PAGE.MATERIAL", "ARROW"), plugin.getIcons().getString("MAIN.NEXT_PAGE.NAME"), lore);
        }
    }
}