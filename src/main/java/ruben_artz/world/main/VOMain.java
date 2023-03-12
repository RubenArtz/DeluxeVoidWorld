package ruben_artz.world.main;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
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
import java.util.*;

@SuppressWarnings("deprecation")
public class VOMain extends JavaPlugin {
    private static VOMain plugin;
    public static VOMain getInstance()
    {
        return plugin;
    }
    PluginDescriptionFile description = getDescription();
    public String version = this.description.getVersion();
    public String table = "deluxe_void_v1";
    public List<String> authors = description.getAuthors();
    public String prefix = "&8[&9Deluxe Void World&8]&f ";
    public VOConfig files;
    public List<UUID> chat = new ArrayList<>();
    public HashMap<String, String> world = new HashMap<>();
    public List<UUID> chat_get = new ArrayList<>();
    public List<UUID> create_world = new ArrayList<>();
    public List<UUID> damage = new ArrayList<>();
    public HashMap<String, String> world_get = new HashMap<>();
    public String latestversion;
    public Set<UUID> IgnoreTeleportation = new HashSet<>();
    public Set<UUID> IgnoreJumping = new HashSet<>();
    public Set<UUID> IgnoreLightning = new HashSet<>();
    public Set<UUID> IgnoreParticles = new HashSet<>();
    public ArrayList<VOArrays> inventory;
    private ArrayList<VOString> message;
    private Launch launch;

    @Override
    public void onEnable() {
        plugin = this;
        try {
            this.launch = Class.forName("ruben_artz.world.launcher.Launcher").asSubclass(Launch.class).newInstance();
            VOManager.syncRunTask(() -> {
                VOMain.this.launch.launch(VOMain.this);
                inventory = new ArrayList<>();
                message = new ArrayList<>();
            });
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
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
        return files;
    }
    public FileConfiguration getIcons() {
        return files.getFile("menus/icons.yml");
    }
    public FileConfiguration getGenerated() {
        return files.getFile("generated.yml");
    }
    public FileConfiguration getWorlds() {
        return files.getFile("worlds.yml");
    }
    public FileConfiguration getMenuHome() {
        return files.getFile("menus/home.yml");
    }
    public FileConfiguration getBoolean() {
        return files.getFile("menus/boolean.yml");
    }
    public FileConfiguration getLagVersion() {
        return files.getFile("lang/version.yml");
    }
    public FileConfiguration getMenuVersion() {
        return files.getFile("menus/version.yml");
    }
    public FileConfiguration getInventoryCrating() {
        return files.getFile("menus/create.yml");
    }
    public ArrayList<VOString> getMessage() {
        return this.message;
    }
    public void addMessage(VOString message) {
        this.message.add(message);
    }
    public void removeMessages() {
        this.message.clear();
    }
    public void LoadAllFiles() {
        initiate();
        saveDefaultConfig();
        reloadConfig();
    }
    public void getReloadPlugin() {
        if (Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.CHECK_UPDATE")).contains("true")) {
            VOUpdater.shutdown();
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, VOUpdater::setEnabled, 20L);
        } else {
            VOUpdater.shutdown();
        }
        HandlerList.unregisterAll(this);
        Launcher.getInstance().registerEvents();
        LoadAllFiles();
    }
    public Set<UUID> getIgnoreJumping() {
        return IgnoreJumping;
    }
    public Set<UUID> getIgnoreTeleportation() {
        return IgnoreTeleportation;
    }
    public Set<UUID> getIgnoreLightning() {
        return IgnoreLightning;
    }
    public Set<UUID> getIgnoreParticles() {
        return IgnoreParticles;
    }
    public  void getCreate(String uuid, String name) {
        world.put(uuid, name);
    }
    public  void getCreateBlockY(String uuid, String name) {
        world_get.put(uuid, name);
    }
    public void sendConsole(String s) {
        Bukkit.getConsoleSender().sendMessage(addColor.setColors(s));
    }
    public String getVersion() {
        return this.version;
    }
    public String getLatestVersion() {
        return this.latestversion;
    }
    public VOArrays getInventory(String name) {
        for (VOArrays inv : inventory) {
            if (inv.getPlayer().getName().equals(name)) {
                return inv;
            }
        }
        return null;
    }
    public void addInventory(VOArrays number) {
        inventory.add(number);
    }
    public void removeInventory(String name) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getPlayer().getName().equals(name)) {
                inventory.clear();
            }
        }
    }
    public void getMessages(){
        VOManager.syncTaskLater(16L, () -> {
            sendConsole("" + prefix + "&aSuccessfully enabled &cv" + version + "");
            sendConsole("&8--------------------------------------------------------------------------------------");
            sendConsole("&7         Developed by &c"+authors+"");
            sendConsole("" + prefix + "&aVersion: &c" + version+" &ais loading... &8(&6Current&8)");
            sendConsole("" + prefix + "&aServer: &c"+Bukkit.getVersion()+"");
            sendConsole("" + prefix + "&aLoading necessary files...");
            sendConsole("&f");
            sendConsole("&9[Loader] &fMaps loaded correctly: &f'&a"+Launcher.getNumberWorlds()+"&f'");
            sendConsole("&f");
            sendConsole("&fDeluxeVoidWorld &aStarting plugin...");
            sendConsole("&f");
            sendConsole("&8--------------------------------------------------------------------------------------");
        });
    }
}
