package ruben_artz.world.main;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import com.tcoded.folialib.FoliaLib;
import developer.voidw.activate;
import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.resolver.data.Repository;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import ruben_artz.world.configuration.VOConfig;
import ruben_artz.world.events.chat.VOString;
import ruben_artz.world.features.addColor;
import ruben_artz.world.launcher.Launch;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.world.VOArrays;
import ruben_artz.world.world.VOGenerator;
import ruben_artz.world.world.VOManager;
import ruben_artz.world.world.VOUpdater;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@SuppressWarnings("deprecation")
public class VOMain extends JavaPlugin {
    private static VOMain plugin;
    public static VOMain getInstance() {
        return plugin;
    }

    @Getter private static TaskScheduler scheduler;
    @Getter private static FoliaLib foliaLib;

    /*
    normal
     */
    @Getter public VOConfig files;
    @Getter public String latestversion;
    @Getter private ArrayList<VOString> message;
    @Getter public ArrayList<VOArrays> inventory;
    @Getter public String table = "deluxe_void_v1";
    @Getter public List<UUID> chat = new ArrayList<>();
    @Getter public List<UUID> damage = new ArrayList<>();
    @Getter public List<UUID> chat_get = new ArrayList<>();
    @Getter public List<UUID> create_world = new ArrayList<>();
    @Getter public String version = getDescription().getVersion();
    @Getter public String prefix = "&8[&9Deluxe Void World&8]&f ";
    @Getter public HashMap<String, String> world = new HashMap<>();
    @Getter public List<String> authors = getDescription().getAuthors();

    /*
    ignore
     */
    @Getter public Set<UUID> IgnoreJumping = new HashSet<>();
    @Getter public Set<UUID> IgnoreLightning = new HashSet<>();
    @Getter public Set<UUID> IgnoreParticles = new HashSet<>();
    @Getter public Set<UUID> IgnoreTeleportation = new HashSet<>();

    private Launch launch;

    @Override
    public void onLoad() {

        getLogger().info("Verifying the dependencies...");

        try {
            Path downloadPath = Paths.get(getDataFolder().getPath() + File.separator + "cache" + File.separator + "libs");
            ApplicationBuilder.appending("DeluxeVoidWorld")
                    //.logger(new SlimJarLogger(this))
                    .downloadDirectoryPath(downloadPath)
                    .mirrorSelector((a, b) -> a)
                    .internalRepositories(Collections.singleton(new Repository(new URL("https://repo1.maven.org/maven2/"))))
                    .build();

            getLogger().info("Dependencies successfully loaded!");
        } catch (ReflectiveOperationException | IOException | URISyntaxException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {

        scheduler = UniversalScheduler.getScheduler(this);
        foliaLib = new FoliaLib(this);
        PaperLib.suggestPaper(this);

        plugin = this;
        try {
            this.launch = Class.forName("ruben_artz.world.launcher.Launcher").asSubclass(Launch.class).newInstance();
            VOManager.syncRunTask(() -> {
                VOMain.this.launch.launch(VOMain.this);
                inventory = new ArrayList<>();
                message = new ArrayList<>();
            });
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
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

    public ChunkGenerator getDefaultWorldGenerator(@Nullable String worldName, String uid) {
        return new VOGenerator();
    }

    public void initiate() {
        files = new VOConfig().initiate(this, "worlds.yml", "generated.yml", "menus/home.yml", "menus/version.yml", "menus/boolean.yml", "menus/create.yml", "menus/icons.yml", "lang/version.yml", "lang/en_US.yml", "lang/es_ES.yml").setLanguageFile("lang/" + getConfig().getString("ADMIN-CONFIG.LANGUAGE") + ".yml");
    }

    public VOConfig getFileTranslations() {
        return getFiles();
    }
    public FileConfiguration getIcons() {
        return getFiles().getFile("menus/icons.yml");
    }
    public FileConfiguration getGenerated() {
        return getFiles().getFile("generated.yml");
    }
    public FileConfiguration getWorlds() {
        return getFiles().getFile("worlds.yml");
    }
    public FileConfiguration getMenuHome() {
        return getFiles().getFile("menus/home.yml");
    }
    public FileConfiguration getBoolean() {
        return getFiles().getFile("menus/boolean.yml");
    }
    public FileConfiguration getLagVersion() {
        return getFiles().getFile("lang/version.yml");
    }
    public FileConfiguration getMenuVersion() {
        return getFiles().getFile("menus/version.yml");
    }
    public FileConfiguration getInventoryCrating() {
        return getFiles().getFile("menus/create.yml");
    }

    public void addMessage(VOString message) {
        this.getMessage().add(message);
    }
    public void removeMessages() {
        this.getMessage().clear();
    }

    public void LoadAllFiles() {
        initiate();
        saveDefaultConfig();
        reloadConfig();
    }
    public void getReloadPlugin() {
        if (Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.CHECK_UPDATE")).contains("true")) {
            VOUpdater.shutdown();
            VOMain.getScheduler().scheduleSyncDelayedTask(VOUpdater::setEnabled, 20L);
        } else {
            VOUpdater.shutdown();
        }
        HandlerList.unregisterAll(this);
        Launcher.getInstance().registerEvents();
        LoadAllFiles();
    }


    public void sendConsole(String s) {
        Bukkit.getConsoleSender().sendMessage(addColor.setColors(s));
    }

    public VOArrays getInventory(String name) {
        for (VOArrays inv : getInventory()) {
            if (inv.getPlayer().getName().equals(name)) {
                return inv;
            }
        }
        return null;
    }
    public void addInventory(VOArrays number) {
        getInventory().add(number);
    }
    public void removeInventory(String name) {
        for (int i = 0; i < getInventory().size(); i++) {
            if (getInventory().get(i).getPlayer().getName().equals(name)) {
                getInventory().clear();
            }
        }
    }
    public void getMessages(){
        VOManager.syncTaskLater(16L, () -> {
            sendConsole(plugin.getPrefix() + "&aSuccessfully enabled &cv" + version);
            sendConsole("&8--------------------------------------------------------------------------------------");
            sendConsole("&7         Developed by &c"+authors);
            sendConsole(plugin.getPrefix() + "&aVersion: &c" + version+" &ais loading... &8(&6Current&8)");
            sendConsole(plugin.getPrefix() + "&aServer: &c"+Bukkit.getVersion());
            sendConsole(plugin.getPrefix() + "&aLoading necessary files...");
            sendConsole("&f");
            sendConsole("&9[Loader] &fMaps loaded correctly: &f'&a"+Launcher.getNumberWorlds()+"&f'");
            sendConsole("&f");
            sendConsole("&fDeluxeVoidWorld &aStarting plugin...");
            sendConsole("&f");
            sendConsole("&8--------------------------------------------------------------------------------------");

            activate.setPolymart();
        });
    }
}