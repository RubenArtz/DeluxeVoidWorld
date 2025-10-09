package ruben_artz.world.configuration;

import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.CrossPlatformUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UpdateConfig {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void update() {
        CrossPlatformUtils.runTask(() -> {
            /*
            Update config of "config.yml"
             */
            if (!Objects.requireNonNull(plugin.getConfig().getString("version")).equals("1.5")) {
                try {
                    Files.copy(Paths.get(plugin.getDataFolder() + "/config.yml"), Paths.get(plugin.getDataFolder() + "/old-config-" + plugin.getConfig().getString("version") + ".yml"), StandardCopyOption.REPLACE_EXISTING);
                    File file = new File(plugin.getDataFolder(), "config.yml");
                    file.delete();
                    plugin.saveDefaultConfig();
                    plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_FILE_UPDATE").replace("{Files}", "config.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            /*
            Update config of "worlds.yml"
             */
            if (!Objects.requireNonNull(plugin.getWorlds().getString("version")).equals("1.5")) {
                try {
                    Files.copy(Paths.get(plugin.getDataFolder() + "/worlds.yml"), Paths.get(plugin.getDataFolder() + "/old-worlds-" + plugin.getConfig().getString("version") + ".yml"), StandardCopyOption.REPLACE_EXISTING);
                    File file = new File(plugin.getDataFolder(), "worlds.yml");
                    file.delete();
                    plugin.initiate();
                    plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_FILE_UPDATE").replace("{Files}", "worlds.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            /*
            Update config of "generated.yml"
             */
            if (!Objects.requireNonNull(plugin.getGenerated().getString("version")).equals("1.1")) {
                try {
                    Files.copy(Paths.get(plugin.getDataFolder() + "/generated.yml"), Paths.get(plugin.getDataFolder() + "/old-generated-" + plugin.getConfig().getString("version") + ".yml"), StandardCopyOption.REPLACE_EXISTING);
                    File file = new File(plugin.getDataFolder(), "generated.yml");
                    file.delete();
                    plugin.initiate();
                    plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_FILE_UPDATE").replace("{Files}", "generated.yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            CrossPlatformUtils.runTaskLater(10L, () -> {
                /*
                Update config of "lang.file"
                 */
                if (!Objects.requireNonNull(plugin.getLagVersion().getString("version")).contains("1.8")) {
                    File f = new File(plugin.getDataFolder(), "/lang/");
                    File[] files = f.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                        }
                    }
                    plugin.initiate();
                    plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_FILE_UPDATE").replace("{Files}", "lang.file"));
                }
            });
            /*
            Update config of "menus.file"
             */
            CrossPlatformUtils.runTaskLater(15L, () -> {
                if (!Objects.requireNonNull(plugin.getMenuVersion().getString("version")).contains("1.7")) {
                    File f = new File(plugin.getDataFolder(), "/menus/");
                    File[] files = f.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                        }
                    }
                    plugin.initiate();
                    plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_FILE_UPDATE").replace("{Files}", "menu.file"));
                }
            });
        });
    }
}
