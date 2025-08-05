package ruben_artz.world.events.world;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.ProjectUtils;
import ruben_artz.world.utils.addColor;

import java.util.List;

public class VODetect implements Listener {
    public final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler
    public void onWorldCreate(WorldInitEvent event) {
        final String worldName = event.getWorld().getName();
        if (plugin.getConfig().getBoolean("ADMIN-CONFIG.AUTOMATICALLY-DETECT-NEW-WORLDS")) {
            if (plugin.getWorlds().get("WORLDS." + worldName + ".ALWAYS-DAY") == null) {
                if (!this.plugin.getConfig().contains(worldName)) {
                    plugin.getWorlds().set("WORLDS." + worldName + ".ALWAYS-DAY", Boolean.TRUE);
                    plugin.getWorlds().set("WORLDS." + worldName + ".TP-WHEN-FALLING", Boolean.TRUE);
                    plugin.getWorlds().set("WORLDS." + worldName + ".VOID-POSITION", -1);
                    plugin.getWorlds().set("WORLDS." + worldName + ".WORLD", "&a" + worldName);
                    plugin.getWorlds().set("WORLDS." + worldName + ".MATERIAL", "STONE");
                    plugin.getWorlds().set("WORLDS." + worldName + ".SPAWN", ProjectUtils.setLocation(worldName));
                    plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.TYPE", "CONSOLE");

                    final List<String> listCommands = plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST");
                    listCommands.add("tell {Player} &cThe lobby is not over there!");
                    plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.LIST", listCommands);

                    plugin.files.saveFile("worlds.yml");
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if ((player.isOp()) || (player.hasPermission("DeluxeVoidWorld.Admin"))) {
                            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NEW_MAP"));
                        }
                    });
                }
            }
        }
    }
}
