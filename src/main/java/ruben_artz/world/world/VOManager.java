package ruben_artz.world.world;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSkull;
import com.cryptomorin.xseries.XSound;
import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import io.papermc.lib.PaperLib;
import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.UltraRegionsAPI;
import me.TechsCode.UltraRegions.storage.EnvironmentalSettings;
import me.TechsCode.UltraRegions.storage.ManagedWorld;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import ruben_artz.world.features.*;
import ruben_artz.world.firework.ColorUtils;
import ruben_artz.world.firework.FireworkManager;
import ruben_artz.world.main.DeluxeVoidWorld;
import ruben_artz.world.menu.VOHome;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VOManager {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static String setPlaceholders(Player player, String text) {
        if (isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static Component setPlaceholders(Player player, Component component) {
        if (!isPluginEnabled("PlaceholderAPI")) return component;

        Pattern placeholderPattern = PlaceholderAPI.getPlaceholderPattern();
        TextReplacementConfig textReplacementConfig = TextReplacementConfig.builder().match(placeholderPattern)
                .replacement((matchResult, builder) -> {
                    String placeholder = matchResult.group(0);
                    String replaced = PlaceholderAPI.setPlaceholders(player, placeholder);
                    return Component.text(replaced);
                }).build();
        return component.replaceText(textReplacementConfig);
    }

    @SuppressWarnings("deprecation")
    public static void sendTextComponent(Player player, String textComponent,
                                         ClickEvent.Action clickEvent, String clickValue,
                                         HoverEvent.Action hoverEvent, String hoverValue) {
        TextComponent message = new TextComponent(addColor.setColors(textComponent));
        message.setClickEvent(new ClickEvent(clickEvent, addColor.setColors(clickValue)));
        message.setHoverEvent(new HoverEvent(hoverEvent, new ComponentBuilder(addColor.setColors(hoverValue)).create()));
        player.spigot().sendMessage(message);
    }

    public static boolean isPluginEnabled(String s) {
        return Bukkit.getPluginManager().getPlugin(s) != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(s)).isEnabled();
    }

    private static void getBossbar(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.BOSSBAR.ENABLED")) {
            sendBossBar.sendBoss(player, plugin.getConfig(), "ON_VOID_TP.SETTINGS.BOSSBAR.BAR_COLOR", plugin.getFileTranslations().getString("MESSAGE_BOSSBAR_TP_LOBBY"));
        }
    }

    public static void geCommands(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.COMMANDS.ENABLED")) {
            if (Objects.requireNonNull(plugin.getConfig().getString("ON_VOID_TP.SETTINGS.COMMANDS.TYPE")).contains("CONSOLE")) {
                for (String commands : plugin.getConfig().getStringList("ON_VOID_TP.SETTINGS.COMMANDS.COMMANDS")) {
                    commands = replacePlaceholder(commands, "{Player}", player.getName());
                    commands = replacePlaceholder(commands, "{Uuid}", player.getUniqueId().toString());
                    commands = replacePlaceholder(commands, "{Address}", Objects.requireNonNull(player.getAddress()).toString());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), addColor.setColors(commands));
                }
            } else {
                for (String commands : plugin.getConfig().getStringList("ON_VOID_TP.SETTINGS.COMMANDS.COMMANDS")) {
                    commands = replacePlaceholder(commands, "{Player}", player.getName());
                    commands = replacePlaceholder(commands, "{Uuid}", player.getUniqueId().toString());
                    commands = replacePlaceholder(commands, "{Address}", Objects.requireNonNull(player.getAddress()).toString());
                    Bukkit.dispatchCommand(player, addColor.setColors(commands));
                }
            }
        }
    }

    public static void getSound(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.SOUNDS.ENABLED")) {
            VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ON_VOID_TP.SETTINGS.SOUNDS.SOUND")), player);
        }
    }

    public static void getTitle(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.TITLES.ENABLED")) {
            for (String Titlelist : plugin.getFileTranslations().getStringList("MESSAGE_TITLE_TP_LOBBY")) {
                Titlelist = replacePlaceholder(Titlelist, "{Player}", player.getName());
                Titlelist = replacePlaceholder(Titlelist, "{Uuid}", player.getUniqueId().toString());
                Titlelist = replacePlaceholder(Titlelist, "{Address}", Objects.requireNonNull(player.getAddress()).toString());
                String[] Title = Titlelist.split(";");
                sendTitles.sendTitle(player, Integer.parseInt(Title[0]), Integer.parseInt(Title[1]), Integer.parseInt(Title[2]), Title[3], Title[4]);
            }
        }
    }

    public static void getMessage(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.MESSAGES.ENABLED")) {
            for (String message : plugin.getFileTranslations().getStringList("MESSAGE_MESSAGES_TP_LOBBY")) {
                message = replacePlaceholder(message, "{Player}", player.getName());
                message = replacePlaceholder(message, "{Uuid}", player.getUniqueId().toString());
                message = replacePlaceholder(message, "{Address}", Objects.requireNonNull(player.getAddress()).toString());
                addColor.sendMessage(player, message);
            }
        }
    }

    public static void getActionbar(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.ACTIONBAR.ENABLED")) {
            String originalMessage = plugin.getFileTranslations().getString("MESSAGE_ACTIONBAR_TP_LOBBY");
            String message = replacePlaceholder(originalMessage, "{Player}", player.getName());
            message = replacePlaceholder(message, "{Uuid}", player.getUniqueId().toString());
            message = replacePlaceholder(message, "{Address}", Objects.requireNonNull(player.getAddress()).toString());
            final String finalMessage = message;
            DeluxeVoidWorld.getFoliaLib().getImpl().runAtLocation(player.getLocation(), wrappedTask ->
                    sendActionbar.sendActionBar(player, finalMessage));
        }
    }

    public static void getFirework(Player player) {
        if (plugin.getConfig().getBoolean("ON_VOID_TP.SETTINGS.FIREWORK.ENABLED")) {
            ArrayList<Color> colors = new ArrayList<>();
            for (String fireworkColor : plugin.getConfig().getStringList("ON_VOID_TP.SETTINGS.FIREWORK.COLORS")) {
                colors.add(ColorUtils.parseColor(fireworkColor));
            }
            if (!colors.isEmpty()) {
                FireworkManager.launchFirework(player.getLocation(), colors);
            }
        }
    }

    public static String replacePlaceholder(String source, String placeholder, String replacement) {
        return Pattern.compile(placeholder, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(source).replaceAll(Matcher.quoteReplacement(replacement));
    }

    public static String setPlaceholders(String message) {
        if ((message == null) || (message.isEmpty())) {
            return message;
        }

        long maximum = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        long total = Runtime.getRuntime().totalMemory();


        long used = total - free;

        long mbUsed = used / 1000000;
        long mbMaximum = maximum / 1000000;
        long mbFree = free / 1000000;

        if (isPluginEnabled("SlimeWorldManager")) {
            message = message.replace("{Plugin}", "Slime World Manager");
        } else if (isPluginEnabled("Multiverse-Core")) {
            message = message.replace("{Plugin}", "Multiverse-Core");
        } else if (isPluginEnabled("UltraRegions")) {
            message = message.replace("{Plugin}", "Ultra Regions");
        } else {
            message = message.replace("{Plugin}", "Deluxe Void World &7- &c&lBETA");
        }
        message = message.replace("{getTPS}", String.valueOf(getTPS()))
                .replace("{Max Players}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{Online Players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{Java Ram}", VOManager.addCommas((int) mbUsed) + " MB")
                .replace("{Java Max Ram}", VOManager.addCommas((int) mbMaximum) + " MB")
                .replace("{Java Free Ram}", VOManager.addCommas((int) mbFree) + " MB");
        return message;
    }

    public static void setGlass(final int slot, final Inventory inventory) {
        final XMaterial mat = XMaterial.BLACK_STAINED_GLASS_PANE;
        ItemStack item = mat.parseItem();
        ItemMeta meta = item != null ? item.getItemMeta() : null;

        if (meta != null) meta.setDisplayName(" ");

        if (item != null) item.setItemMeta(meta);

        inventory.setItem(slot, item);
    }

    public static void setSkullTexture(final int slot, final Inventory inventory, final String name, final List<String> lore, final String texture) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta skullMeta = (SkullMeta) (item != null ? item.getItemMeta() : null);

        if (skullMeta != null) skullMeta.setDisplayName(addColor.setColors(setPlaceholders(name)));

        lore.replaceAll(s -> addColor.setColors(setPlaceholders(s)));
        if (skullMeta != null) skullMeta.setLore(lore);

        if (item != null && skullMeta != null) item.setItemMeta(XSkull.of(skullMeta).profile(XSkull.SkullInputType.TEXTURE_HASH, texture).apply());

        inventory.setItem(slot, item);
    }

    public static void setItem(final int slot, final Inventory inventory, final String material, final String name, final List<String> lore) {
        final XMaterial mat = XMaterial.valueOf(material);
        ItemStack item = mat.parseItem();
        ItemMeta meta = item != null ? item.getItemMeta() : null;

        if (meta != null) meta.setDisplayName(addColor.setColors(setPlaceholders(name)));

        lore.replaceAll(s -> addColor.setColors(setPlaceholders(s)));
        if (meta != null) meta.setLore(lore);

        if (item != null) item.setItemMeta(meta);

        inventory.setItem(slot, item);
    }
    public static void setItem(final int slot, final Inventory inventory, final String material, final String name, final List<String> lore, int amount) {
        final XMaterial mat = XMaterial.valueOf(material);
        ItemStack item = mat.parseItem();
        ItemMeta meta = item != null ? item.getItemMeta() : null;

        if (meta != null) meta.setDisplayName(addColor.setColors(setPlaceholders(name)));

        lore.replaceAll(s -> addColor.setColors(setPlaceholders(s)));
        if (meta != null) meta.setLore(lore);

        if (meta != null) item.setAmount(amount);

        if (item != null) item.setItemMeta(meta);

        inventory.setItem(slot, item);
    }

    public static void getMessagesArgs(Player player) {
        VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CLICK_COMMAND_HELP")), player);
        String box = plugin.getFileTranslations().getString("MESSAGE_CLICK_COMMAND_BOX");
        player.sendMessage(addColor.setColors("&8&m--------------------------------------------------"));
        sendTextComponent(player, plugin.getFileTranslations().getString("MESSAGE_CLICK_COMMAND").replace("{Version}", plugin.getVersion())
                , ClickEvent.Action.RUN_COMMAND
                , "/dew help"
                , HoverEvent.Action.SHOW_TEXT
                , box);
        player.sendMessage(addColor.setColors("&8&m--------------------------------------------------"));
    }

    public static void getMessagesArgsConsole(CommandSender sender) {
        sender.sendMessage(addColor.setColors("&8&m--------------------------------------------------"));
        sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_CLICK_COMMAND").replace("{Version}", plugin.getVersion())));
        sender.sendMessage(addColor.setColors("&8&m--------------------------------------------------"));
    }

    public static void getHelpCommandGame(Player sender) {
        VOManager.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.MESSAGE_CLICK_HELP_COMMANDS")), sender);
        sender.sendMessage(addColor.setColors("&8« » ============== &e✯ &9&lDeluxe Void World &e✯ &8============== « »"));
        sender.sendMessage(addColor.setColors(plugin.getFileTranslations().getString("MESSAGE_USE_COMMANDS_TIP")));
        sender.sendMessage(addColor.setColors("&f"));
        sendTextComponent(sender, " &8▪ &f/dew reload &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_RELOAD")
                , ClickEvent.Action.RUN_COMMAND
                , "/dew reload"
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_RELOAD_BOX").replace("{line}", "\n"));
        sendTextComponent(sender, " &8▪ &f/dew help &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_HELP")
                , ClickEvent.Action.RUN_COMMAND
                , "/dew help"
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_HELP_BOX").replace("{line}", "\n"));
        sendTextComponent(sender, " &8▪ &f/dew editor &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_GUI")
                , ClickEvent.Action.RUN_COMMAND
                , "/dew editor"
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_GUI_BOX").replace("{line}", "\n"));
        sendTextComponent(sender, " &8▪ &f/dew toggle <world> &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_TOGGLE")
                , ClickEvent.Action.SUGGEST_COMMAND
                , "/dew toggle "
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_TOGGLE_BOX").replace("{line}", "\n"));
        sendTextComponent(sender, " &8▪ &f/dew setworldvoid &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_SET_WORLD")
                , ClickEvent.Action.RUN_COMMAND
                , "/dew setworldvoid"
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_SET_WORLD_BOX").replace("{line}", "\n"));
        sendTextComponent(sender, " &8▪ &f/dew teleport <world> &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_TELEPORT")
                , ClickEvent.Action.SUGGEST_COMMAND
                , "/dew teleport "
                , HoverEvent.Action.SHOW_TEXT
                , plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_TELEPORT_BOX").replace("{line}", "\n"));
        sender.sendMessage(addColor.setColors(""));
        sender.sendMessage(addColor.setColors("&8================================================="));
    }

    public static void getHelpCommandConsole(CommandSender player) {
        player.sendMessage(addColor.setColors("&8« » ============== &e✯ &9&lDeluxe Void World &e✯ &8============== « »"));
        player.sendMessage(addColor.setColors("&f"));
        player.sendMessage(addColor.setColors(" &8▪ &f/dew reload &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_RELOAD")));
        player.sendMessage(addColor.setColors(" &8▪ &f/dew help &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_HELP")));
        player.sendMessage(addColor.setColors(" &8▪ &f/dew toggle <world> &7» " + plugin.getFileTranslations().getString("MESSAGE_USE_COMMAND_TOGGLE")));
        player.sendMessage(addColor.setColors(""));
        player.sendMessage(addColor.setColors("&8================================================="));
    }

    // Teleporting the player into a world
    public static void setTeleportation(Player player) {
        Location location = returnLocation(Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + player.getWorld().getName() + ".SPAWN")));
        PaperLib.teleportAsync(player, location);
    }

    // Use the jump option
    public static void getJump(Player player) {
        double boost = plugin.getConfig().getInt("ON_VOID_TP.SETTINGS.JUMP.BOOST");
        Vector launch = new Vector(0, boost, 0);
        player.setVelocity(launch);
    }

    public static void getJumpEffects(Player player) {
        VOManager.getJump(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> VOManager.getJump(player), 10L);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> VOManager.getJump(player), 20L);
    }

    // Use the Lightning option
    public static void getLightningEffect(Player player) {
        Location location = returnLocation(Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + player.getWorld().getName() + ".SPAWN")));
        getTeleportation(player);
        player.getWorld().strikeLightningEffect(location);
    }

    // Use the normal Teleportation option
    public static void getTeleportation(Player player) {
        setTeleportation(player);
        plugin.getDamage().add(player.getUniqueId());
        geCommands(player);
        getSound(player);
        getBossbar(player);
        getTitle(player);
        getMessage(player);
        getActionbar(player);
    }

    // Use the particle option
    public static void getParticles(Player player) {
        setTeleportation(player);
        plugin.getDamage().add(player.getUniqueId());
        geCommands(player);
        getSound(player);
        getBossbar(player);
        getTitle(player);
        getMessage(player);
        getActionbar(player);
        getFirework(player);
        sendParticles.sendParticle(player);
    }

    // Check if the player deactivated all teleporting options
    public static void isNull(Player player) {
        if (plugin.getIgnoreTeleportation().contains(player.getUniqueId()) &&
                (plugin.getIgnoreJumping().contains(player.getUniqueId())) &&
                (plugin.getIgnoreParticles().contains(player.getUniqueId())) &&
                (plugin.getIgnoreLightning().contains(player.getUniqueId()))) {
            setTeleportation(player);
        }
    }

    /*
     * Creating worlds with the Multiverse Core API
     */
    public static void createWorldWithMultiverse(Player player, String name, String type) {
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        MVWorldManager worldManager = null;
        if (core != null) worldManager = core.getMVWorldManager();
        if (Bukkit.getWorld(name) == null) {
            if (worldManager != null) worldManager.addWorld(name, World.Environment.valueOf(type), null, WorldType.FLAT, false, "DeluxeVoidWorld");
            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_TP_NEW_MAP"));
            teleportPlayer(player , name);
        } else {
            VOHome.getInventory(player, 1);
            addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_MAP_ALREADY_EXISTS"));
        }
    }

    /*
     * Create an empty world from the player's gui
     */
    public static void createWorldsDeluxe(Player player, String name, String type) {
        syncRunTask(() -> {
            if (Bukkit.getWorld(name) == null) {
                // Adding the world name in config.yml
                ArrayList<String> worlds = (ArrayList<String>) plugin.getConfig().getStringList("ON_VOID_TP.DO_NOT_TOUCH_WORLDS");
                worlds.add(setLocation(name, type));
                plugin.getConfig().set("ON_VOID_TP.DO_NOT_TOUCH_WORLDS", worlds);
                plugin.saveConfig();
                plugin.reloadConfig();
                /*
                Add the world name in generated.yml
                 */
                plugin.getGenerated().set("WORLDS." + name + ".ENVIROMENT", type);
                plugin.getGenerated().set("WORLDS." + name + ".WORLD-TYPE", "FLAT");
                plugin.getGenerated().set("WORLDS." + name + ".DIFFICULTY", "PEACEFUL");
                plugin.getGenerated().set("WORLDS." + name + ".SPAWN-FLAGS", true);
                plugin.getGenerated().set("WORLDS." + name + ".PVP", true);
                plugin.getGenerated().set("WORLDS." + name + ".STORM", false);
                plugin.getGenerated().set("WORLDS." + name + ".THUNDERING", false);
                plugin.getGenerated().set("WORLDS." + name + ".WEATHER-DURATION", 2147483647);
                plugin.getGenerated().set("WORLDS." + name + ".BORDER-SIZE", 300);
                plugin.getGenerated().set("WORLDS." + name + ".AUTO-SAVE", false);
                plugin.getGenerated().set("WORLDS." + name + ".SPAWN", setLocation(name));
                plugin.files.saveFile("generated.yml");
                /*
                Create worldA
                 */
                createEmptyWorld(name, type, "FLAT", "PEACEFUL", true, true, false, false, 2147483647, false, 300);
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_TP_NEW_MAP"));
                teleportPlayer(player , name);
            } else {
                VOHome.getInventory(player, 1);
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_MAP_ALREADY_EXISTS"));
            }
        });
    }

    // Creating completely empty/loaded worlds
    @SuppressWarnings("deprecation")
    public static void createEmptyWorld(String name, String type, String worldtype, String difficulty, boolean spawnFlags, boolean pvp, boolean storm, boolean thundering, int weatherDuration, boolean autoSave, int borderSize) {
        WorldCreator creator = new WorldCreator(name);
        creator.environment(World.Environment.valueOf(type));
        creator.type(WorldType.valueOf(worldtype));
        creator.generator(new VOGenerator());
        World world = creator.createWorld();
        assert world != null;
        WorldBorder worldBorder = world.getWorldBorder();
        world.setDifficulty(Difficulty.valueOf(difficulty));
        world.setSpawnFlags(spawnFlags, spawnFlags);
        world.setPVP(pvp);
        world.setStorm(storm);
        world.setThundering(thundering);
        world.setWeatherDuration(weatherDuration);
        world.setTicksPerAnimalSpawns(1);
        world.setTicksPerMonsterSpawns(1);
        world.setAutoSave(autoSave);
        worldBorder.setCenter(0, 0);
        worldBorder.setSize(borderSize);
        if (isVersion_1_16_To_1_19()) {
            world.setGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE.getName(), "false");
            world.setGameRuleValue(GameRule.MOB_GRIEFING.getName(), "false");
            world.setGameRuleValue(GameRule.DO_FIRE_TICK.getName(), "false");
            world.setGameRuleValue(GameRule.SHOW_DEATH_MESSAGES.getName(), "false");
            world.setGameRuleValue(GameRule.RANDOM_TICK_SPEED.getName(), "0");
        }
        world.setSpawnLocation(0, 80, 0);
    }

    public static void createUltraWorld(Player player, String name, String environment) {
        final UltraRegionsAPI ultraRegionsAPI = UltraRegions.getAPI();
        ultraRegionsAPI.getWorldLoader().generateWorld(name, new EnvironmentalSettings(
                World.Environment.valueOf(environment),
                WorldType.FLAT,
                false, "DeluxeVoidWorld",
                0)
        );
        teleportPlayer(player , name);
        syncTaskLater(70, () -> {
            Optional<ManagedWorld> optional = ultraRegionsAPI.getWorlds().find(player.getWorld());
            if (optional.isPresent()) {
                optional.get().setDifficulty(Difficulty.PEACEFUL);
                optional.get().setLoadOnStartup(true);
                optional.get().setTimeLocked(true);
                optional.get().setWeatherLocked(true);
                optional.get().setIcon(me.TechsCode.UltraRegions.base.item.XMaterial.ENDER_PEARL);
                optional.get().setGamemode(GameMode.CREATIVE);
            }
        });
    }

    private static void teleportPlayer(Player player, String name) {
        syncTaskLater(60L, () -> {
            Location location = returnLocation(Objects.requireNonNull(plugin.getWorlds().getString("WORLDS." + name + ".SPAWN")));
            PaperLib.teleportAsync(player, location).thenAccept(result -> {
                if (result) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
            });
        });
    }

    // Save worlds
    public static void saveWorlds(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            world.save();
        }
    }

    // Gets the map coordinates
    public static String setLocation(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + "," + (double) location.getBlockX() + "," + (double) location.getBlockY() + "," + (double) location.getBlockZ() + "," + (int) location.getYaw() + "," + (int) location.getPitch();
    }

    // Gets the map coordinates
    public static String setLocation(String custom) {
        return custom + "," + 0 + "," + 80 + "," + 0 + "," + 0.0 + "," + 0.0;
    }

    public static String setLocation(String custom, String type) {
        return custom + "," + 0 + "," + 80 + "," + 0 + "," + 0.0 + "," + 0.0 + "," + type;
    }

    // Return map coordinates
    public static Location returnLocation(String name) {
        String[] split = name.split(",");
        World world = Bukkit.getServer().getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    // get TPS server
    public static String getTPS() {
        final DecimalFormat decimalFormat = new DecimalFormat("##.##");
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        Object invoke;
        try {
            if (minecraftVersion() >= 17) {
                invoke = Class.forName("net.minecraft.server.MinecraftServer").getMethod("getServer").invoke(null);
                final double n = ((double[]) invoke.getClass().getField("recentTps").get(invoke))[0];
                return (n < 20.0) ? decimalFormat.format(n) : "*20.00";
            } else {
                invoke = Class.forName("net.minecraft.server." + name.substring(name.lastIndexOf(46) + 1) + "." + "MinecraftServer").getMethod("getServer").invoke(null);
                final double n = ((double[]) invoke.getClass().getField("recentTps").get(invoke))[0];
                return (n < 20.0) ? decimalFormat.format(n) : "*20.00";
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException ex) {
            return "N/A";
        }
    }

    // get Minecraft version
    public static int minecraftVersion() {
        try {
            final Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)").matcher(Bukkit.getVersion());
            if (matcher.find()) {
                return Integer.parseInt(matcher.toMatchResult().group(2), 10);
            }
            throw new IllegalArgumentException(String.format("No match found in '%s'", Bukkit.getVersion()));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Failed to determine Minecraft version", ex);
        }
    }

    /*
    get size of paths configurations
     */
    public static int getWorldPathSize() {
        return Objects.requireNonNull(plugin.getWorlds().getConfigurationSection("WORLDS")).getKeys(false).size();
    }

    /*
    get maths of pages
     */
    public static int getInventoryPages() {
        int page;
        if (getWorldPathSize() % 45 == 0) {
            page = (getWorldPathSize() / 45);
        } else {
            page = (getWorldPathSize() / 45 + 1);
        }
        return page;
    }

    /*
     * Add obscure effect
     */
    public static void sendBackGround(String type,Player player) {
        if (type.equalsIgnoreCase("ADD")) {
            PotionEffect potionEffectAdd = new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 5, false, false);
            player.addPotionEffect(potionEffectAdd);
        } else if (type.equalsIgnoreCase("REMOVE")) {
            syncTaskLater(plugin.getConfig().getInt("ON_VOID_TP.SETTINGS.BACKGROUND_EFFECT.TIME-REMOVE"), () -> player.removePotionEffect(PotionEffectType.BLINDNESS));
        }
    }

    /*
     * add Commas
     */
    public static String addCommas(int number) {
        String comma = ",";
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String format = decimalFormat.format(number);
        int integer = 0;
        if (format.indexOf(".") == format.length()-2 && format.charAt(format.length()-1) == '0' && format.length() >= 3) {
            format = format.substring(0, format.length()-2);
        }
        String mn = format.contains(".") ? format.substring(0, format.indexOf(".")) : format;
        for (int i = mn.length()-1; i > 0; i--) {
            integer++;
            if (integer % 3 != 0) continue;
            mn = mn.substring(0, i)+comma+mn.substring(i);
        }
        if (format.contains(".")) {
            format = mn+format.substring(format.indexOf("."));
        } else {
            format = mn;
        }
        if (format.charAt(format.length()-1) == ',') {
            format = format.substring(0, format.length()+1);
        }
        return format;
    }

    /*
     * It will check if they are using versions 1.10-1.19.
     */
    public static boolean isVersion_1_10_To_1_19() {
        return Bukkit.getVersion().contains("1.10")
                || (Bukkit.getVersion().contains("1.11"))
                || (Bukkit.getVersion().contains("1.12"))
                || (Bukkit.getVersion().contains("1.13"))
                || (Bukkit.getVersion().contains("1.14"))
                || (Bukkit.getVersion().contains("1.15"))
                || (Bukkit.getVersion().contains("1.16"))
                || (Bukkit.getVersion().contains("1.17"))
                || (Bukkit.getVersion().contains("1.18"))
                || (Bukkit.getVersion().contains("1.19"))
                || (Bukkit.getVersion().contains("1.20"));
    }

    public static boolean isVersion_1_16_To_1_19() {
        return (Bukkit.getVersion().contains("1.16"))
                || (Bukkit.getVersion().contains("1.17"))
                || (Bukkit.getVersion().contains("1.18"))
                || (Bukkit.getVersion().contains("1.19"))
                || (Bukkit.getVersion().contains("1.20"));
    }

    public static boolean isMySQL() {
        return Objects.equals(plugin.getConfig().getString("ADMIN-CONFIG.DATABASE.ENABLED"), "true");
    }

    public static void syncRunTask(Runnable runnable) {
        DeluxeVoidWorld.getScheduler().runTask(runnable);
    }

    public static void syncTaskLater(long delay, Runnable runnable) {
        DeluxeVoidWorld.getScheduler().runTaskLater(runnable, delay);
    }

    public static void synTaskAsynchronously(Runnable runnable) {
        DeluxeVoidWorld.getScheduler().runTaskAsynchronously(runnable);
    }

    public static MyScheduledTask syncRepeatingTask(long time, Runnable runnable) {
        return DeluxeVoidWorld.getScheduler().runTaskTimer(runnable, 0L, time);
    }

    public static MyScheduledTask syncRunTaskTimer(long time, Runnable runnable) {
        return DeluxeVoidWorld.getScheduler().runTaskTimer(runnable, 0, time);
    }

    public static void syncDelayedTask(int delay, Runnable runnable) {
        DeluxeVoidWorld.getScheduler().runTaskLater(runnable, delay);
    }

    public static void syncDelayedTask(long time, Runnable runnable) {
        DeluxeVoidWorld.getScheduler().runTaskLater(runnable, time);
    }

    public static void executeSound(@Nonnull String path, final Player player) {
        final String string = "ItemsAdder_Sound: ";
        if (path.startsWith(string)) {
            if (path.contains(", ")) {
                final String[] line = path.replace(string, "").split(", ");
                player.playSound(player.getLocation(), line[0], Float.parseFloat(line[1]), Float.parseFloat(line[2]));
                return;
            }
            final String line = path.replace(string, "");

            XSound.play(line, soundPlayer -> soundPlayer.forPlayers(player));
        } else {
            XSound.play(path, soundPlayer -> soundPlayer.forPlayers(player));
        }
    }
}