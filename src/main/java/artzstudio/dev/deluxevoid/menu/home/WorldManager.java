package artzstudio.dev.deluxevoid.menu.home;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import dev.dejvokep.boostedyaml.block.implementation.Section;

import java.util.*;

public class WorldManager {

    public static final Map<UUID, String> editingWorldSession = new HashMap<>();
    private final DeluxeVoidWorld plugin;

    public WorldManager(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
    }

    public List<String> getSortedWorlds() {
        List<String> worlds = new ArrayList<>();
        Section section = plugin.getWorlds().getSection("WORLDS");

        if (section == null) return worlds;

        for (Object key : section.getKeys()) {
            worlds.add(key.toString());
        }

        Collections.sort(worlds);
        return worlds;
    }

    public void setEditingWorld(UUID uuid, String worldName) {
        editingWorldSession.put(uuid, worldName);
    }

    public String getEditingWorld(UUID uuid) {
        return editingWorldSession.get(uuid);
    }
}