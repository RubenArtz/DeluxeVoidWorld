package artzstudio.dev.deluxevoid.menu.icon;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IconManager {

    private static final List<String> cachedMaterials = new ArrayList<>();

    public IconManager() {
        loadMaterials();
    }

    private void loadMaterials() {
        if (!cachedMaterials.isEmpty()) return;

        for (XMaterial mat : XMaterial.values()) {
            if (!mat.isSupported()) continue;

            if (mat == XMaterial.AIR || mat == XMaterial.CAVE_AIR || mat == XMaterial.VOID_AIR) continue;

            try {
                ItemStack item = mat.parseItem();

                if (item == null || item.getType() == Material.AIR) continue;

                cachedMaterials.add(mat.name());

            } catch (IllegalArgumentException | NullPointerException ignored) {
            }
        }

        Collections.sort(cachedMaterials);
    }

    public List<String> getSortedMaterials() {
        return cachedMaterials;
    }
}