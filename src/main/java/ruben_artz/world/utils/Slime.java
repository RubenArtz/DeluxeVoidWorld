package ruben_artz.world.utils;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ruben_artz.world.features.addColor;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.menu.Home;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ConstantConditions")
public class Slime {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    private static SlimePlugin slime;

    public Slime() {
        Slime.slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    }

    public static void createWorldWithSlime(Player player, String world, String type) {
        SlimePropertyMap propertyMap = new SlimePropertyMap();
        SlimeLoader slimeLoader = slime.getLoader("file");
        propertyMap.setString(SlimeProperties.WORLD_TYPE, "flat");
        propertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        propertyMap.setInt(SlimeProperties. SPAWN_Y, 80);
        propertyMap.setInt(SlimeProperties.SPAWN_Z, 0);
        propertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        propertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        propertyMap.setString(SlimeProperties.DIFFICULTY, "easy");
        propertyMap.setBoolean(SlimeProperties.PVP, true);
        propertyMap.setString(SlimeProperties.ENVIRONMENT, type);
        try {
            if (slimeLoader.worldExists(world)) {
                Home.getInventory(player, 1);
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_MAP_ALREADY_EXISTS"));
            } else {
                slime.createEmptyWorld(slimeLoader, world, false, propertyMap);
                File configFile = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "/plugins/SlimeWorldManager/worlds.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                config.set("worlds."+world+".source", "file");
                config.set("worlds."+world+".spawn", "0, 80, 0");
                config.set("worlds."+world+".difficulty", "easy");
                config.set("worlds."+world+".allowMonsters", false);
                config.set("worlds."+world+".allowAnimals", false);
                config.set("worlds."+world+".environment", type);
                config.set("worlds."+world+".worldType", "DEFAULT");
                config.set("worlds."+world+".loadOnStartup", true);
                config.set("worlds."+world+".readOnly", false);
                config.save(configFile);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm reload");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm load "+world);
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_TP_NEW_MAP"));

                ProjectUtils.runTaskLater(60L, () -> {
                    Location location = ProjectUtils.returnLocation(plugin.getWorlds().getString("WORLDS." + world + ".SPAWN"));
                    player.teleport(location);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                });
            }
        } catch (IOException | WorldAlreadyExistsException exception) {
            throw new RuntimeException();
        }
    }
}
