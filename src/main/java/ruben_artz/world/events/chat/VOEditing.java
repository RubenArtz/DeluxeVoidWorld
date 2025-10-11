package ruben_artz.world.events.chat;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.features.sendTitles;
import ruben_artz.world.menu.Create;
import ruben_artz.world.menu.Home;
import ruben_artz.world.utils.UtilityFunctions;
import ruben_artz.world.utils.addColor;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("IfStatementWithIdenticalBranches")
public class VOEditing implements Listener {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public static MyScheduledTask getBlockX;
    public static MyScheduledTask announce;
    public static MyScheduledTask create;

    @EventHandler
    public void getCancelEdition(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Remove chat editor
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.getChat().contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    UtilityFunctions.runTask(() -> Home.getInventory(player, 1));
                } else {
                    Home.getInventory(player, 1);
                }
                plugin.getChat().clear();
                announce.cancel();
                sendTitles.clearTitle(player);
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.getChat_get().contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    UtilityFunctions.runTask(() -> Home.getInventory(player, 1));
                } else {
                    Home.getInventory(player, 1);
                }
                plugin.getChat_get().clear();
                getBlockX.cancel();
                sendTitles.clearTitle(player);
            }
        }
        // Remove chat creation of world
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.getCreate_world().contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    UtilityFunctions.runTask(() -> Home.getInventory(player, 1));
                } else {
                    Home.getInventory(player, 1);
                }
                plugin.getCreate_world().clear();
                plugin.removeMessages();
                create.cancel();
                sendTitles.clearTitle(player);
            }
        }
    }

    @EventHandler
    public void createNewWorld(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (plugin.getCreate_world().contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.addMessage(new VOString(player.getName(), message));
            if (event.isAsynchronous()) {
                UtilityFunctions.runTask(() -> Create.openInventory(player));
            } else {
                UtilityFunctions.runTask(() -> Create.openInventory(player));
            }
            create.cancel();
            sendTitles.clearTitle(player);
        }
    }

    // add world to worlds.yml
    @EventHandler
    public void addWorldInventory(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getChat().contains(player.getUniqueId())) {
            event.setCancelled(true);
            final String worldName = event.getMessage();
            plugin.getWorlds().set("WORLDS." + worldName + ".ALWAYS-DAY", Boolean.TRUE);
            plugin.getWorlds().set("WORLDS." + worldName + ".TP-WHEN-FALLING", Boolean.TRUE);
            plugin.getWorlds().set("WORLDS." + worldName + ".VOID-POSITION", -5);
            plugin.getWorlds().set("WORLDS." + worldName + ".WORLD", "&a" + worldName);
            plugin.getWorlds().set("WORLDS." + worldName + ".MATERIAL", "STONE");
            plugin.getWorlds().set("WORLDS." + worldName + ".SPAWN", UtilityFunctions.setLocation(worldName));
            plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.TYPE", "CONSOLE");

            final List<String> listCommands = plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST");
            listCommands.add("tell {Player} &cThe lobby is not over there!");
            plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.LIST", listCommands);

            plugin.files.saveFile("worlds.yml");
            if (event.isAsynchronous()) {
                UtilityFunctions.runTask(() -> {
                    Home.getInventory(player, 1);
                    UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CREATE_WORLD_MENU")), player);
                });
            } else {
                Home.getInventory(player, 1);
                UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CREATE_WORLD_MENU")), player);
            }
            plugin.getChat().clear();
            announce.cancel();
            sendTitles.clearTitle(player);
        }
    }

    // edit coordinates
    @EventHandler
    public void getEditBlockY(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.chat_get.contains(player.getUniqueId())) {
            event.setCancelled(true);
            try {
                int number = Integer.parseInt(event.getMessage());
                if (number < 51) {
                    plugin.getWorlds().set("WORLDS." + player.getWorld().getName() + ".VOID-POSITION", Integer.parseInt(event.getMessage()));
                    plugin.files.saveFile("worlds.yml");
                    if (event.isAsynchronous()) {
                        UtilityFunctions.runTask(() -> Home.getInventory(player, 1));
                    } else {
                        Home.getInventory(player, 1);
                    }
                    getBlockX.cancel();
                    plugin.chat_get.clear();
                    sendTitles.clearTitle(player);
                } else {
                    UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CHANGE_Y_LESS_ZERO")), player);
                    addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_LESS_ZERO"));
                }
            } catch (NumberFormatException e) {
                UtilityFunctions.executeSound(Objects.requireNonNull(plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CHANGE_Y_NO_WORDS")), player);
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NO_WORDS").replace("{Word}", event.getMessage()));
            }
        }
    }

    @EventHandler
    public void getChatLeave(PlayerQuitEvent event) {
        if (getBlockX != null) {
            getBlockX.cancel();
        }
        plugin.getChat_get().clear();

        if (announce != null) {
            announce.cancel();
        }
        plugin.getChat().clear();

        if (create != null) {
            create.cancel();
        }
        plugin.getCreate_world().clear();
    }
}