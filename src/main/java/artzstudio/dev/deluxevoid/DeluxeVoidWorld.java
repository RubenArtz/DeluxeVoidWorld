package artzstudio.dev.deluxevoid;

import artzstudio.dev.deluxevoid.configuration.ConfigType;
import artzstudio.dev.deluxevoid.configuration.ConfigurationManager;
import artzstudio.dev.deluxevoid.data.player.PlayerManager;
import artzstudio.dev.deluxevoid.data.world.WorldManager;
import artzstudio.dev.deluxevoid.launcher.Launch;
import artzstudio.dev.deluxevoid.launcher.Launcher;
import artzstudio.dev.deluxevoid.session.SessionManager;
import artzstudio.dev.deluxevoid.utils.Generator;
import artzstudio.dev.deluxevoid.utils.addColor;
import artzstudio.dev.deluxevoid.utils.slim.SlimJar;
import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeluxeVoidWorld extends JavaPlugin {
    private static DeluxeVoidWorld plugin;

    @Getter
    private static TaskScheduler scheduler;
    @Getter
    public String latestversion;
    @Getter
    public String table = "deluxe_void_v1";

    @Getter
    public SessionManager sessionManager;
    @Getter
    public PlayerManager playerManager;

    @Getter
    public String version = getDescription().getVersion();
    @Getter
    public String prefix = "&8[&9Deluxe Void World&8]&f ";
    @Getter
    public HashMap<String, String> world = new HashMap<>();
    @Getter
    public List<String> authors = getDescription().getAuthors();

    /*
     * normal
     */
    @Getter
    private ConfigurationManager configManager;
    @Getter
    private ArrayList<WorldManager> message;

    private Launch launch;

    public static DeluxeVoidWorld getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        SlimJar.load(this);
    }

    @Override
    public void onEnable() {
        scheduler = UniversalScheduler.getScheduler(this);
        plugin = this;

        try {
            this.launch = Class.forName("artzstudio.dev.deluxevoid.launcher.Launcher")
                    .asSubclass(Launch.class)
                    .getDeclaredConstructor()
                    .newInstance();

            DeluxeVoidWorld.this.launch.launch(DeluxeVoidWorld.this);
            message = new ArrayList<>();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (this.launch != null) {
            this.launch.shutdown();
            this.launch = null;
            plugin = null;
        }
    }

    public Audience getAudiences(Player player) {
        return Launcher.getInstance().audiences.player(player);
    }

    public Audience getAudiences() {
        return Launcher.getInstance().audiences.console();
    }

    public ChunkGenerator getDefaultWorldGenerator(@Nullable String worldName, String uid) {
        return new Generator();
    }

    public void initiate() {
        this.configManager = new ConfigurationManager(this);
        this.configManager.loadAll();
    }

    public YamlDocument getFileTranslations() {
        String selectedLang = getConfigYaml().getString("ADMIN-CONFIG.LANGUAGE", "en_US");
        return "es_ES".equals(selectedLang)
                ? configManager.get(ConfigType.LANG_ES)
                : configManager.get(ConfigType.LANG_EN);
    }

    // General
    public YamlDocument getConfigYaml() {
        return configManager.get(ConfigType.CONFIG);
    }

    public YamlDocument getGenerated() {
        return configManager.get(ConfigType.GENERATED);
    }

    public YamlDocument getWorlds() {
        return configManager.get(ConfigType.WORLD);
    }

    // Menus
    public YamlDocument getMenuHome() {
        return configManager.get(ConfigType.HOME);
    }

    public YamlDocument getIcons() {
        return configManager.get(ConfigType.ICONS);
    }

    public YamlDocument getBoolean() {
        return configManager.get(ConfigType.BOOLEAN);
    }

    public YamlDocument getInventoryCrating() {
        return configManager.get(ConfigType.CREATE);
    }

    public void addMessage(WorldManager message) {
        this.getMessage().add(message);
    }

    public void removeMessages() {
        this.getMessage().clear();
    }

    public void getReloadPlugin() {
        HandlerList.unregisterAll(this);
        Launcher.getInstance().registerEvents();

        configManager.reloadAll();
    }

    public void sendConsole(String s) {
        Audience audience = plugin.getAudiences();
        audience.sendMessage(addColor.addColors(s));
    }
}