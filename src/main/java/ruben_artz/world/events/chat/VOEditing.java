package ruben_artz.world.events.chat;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ruben_artz.world.features.addColor;
import ruben_artz.world.features.sendTitles;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.menu.VOCreate;
import ruben_artz.world.menu.VOHome;
import ruben_artz.world.world.VOManager;

import java.util.List;

@SuppressWarnings("IfStatementWithIdenticalBranches")
public class VOEditing implements Listener {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);
    public static int getBlockX;
    public static int announce;
    public static int create;

    @EventHandler
    public void getCancelEdition(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Remove chat editor
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.chat.contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    VOManager.syncRunTask(() -> VOHome.getInventory(player, 1));
                } else {
                    VOHome.getInventory(player, 1);
                }
                plugin.chat.clear();
                Bukkit.getServer().getScheduler().cancelTask(announce);
                sendTitles.clearTitle(player);
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.chat_get.contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    VOManager.syncRunTask(() -> VOHome.getInventory(player, 1));
                } else {
                    VOHome.getInventory(player, 1);
                }
                plugin.chat_get.clear();
                Bukkit.getServer().getScheduler().cancelTask(getBlockX);
                sendTitles.clearTitle(player);
            }
        }
        // Remove chat creation of world
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (plugin.create_world.contains(player.getUniqueId())) {
                if (event.isAsynchronous()) {
                    VOManager.syncRunTask(() -> VOHome.getInventory(player, 1));
                } else {
                    VOHome.getInventory(player, 1);
                }
                plugin.create_world.clear();
                plugin.removeMessages();
                Bukkit.getServer().getScheduler().cancelTask(create);
                sendTitles.clearTitle(player);
            }
        }
    }

    @EventHandler
    public void createNewWorld(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (plugin.create_world.contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.addMessage(new VOString(player.getName(), message));
            if (event.isAsynchronous()) {
                VOManager.syncRunTask(() -> VOCreate.openInventory(player));
            } else {
                VOManager.syncRunTask(() -> VOCreate.openInventory(player));
            }
            Bukkit.getServer().getScheduler().cancelTask(create);
            sendTitles.clearTitle(player);
        }
    }

    // add world to worlds.yml
    @EventHandler
    public void addWorldInventory(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.chat.contains(player.getUniqueId())) {
            event.setCancelled(true);
            final String worldName = event.getMessage();
            plugin.getCreate(player.getUniqueId().toString(), worldName);
            plugin.getWorlds().set("WORLDS." +worldName+ ".ALWAYS-DAY", Boolean.TRUE);
            plugin.getWorlds().set("WORLDS." +worldName+ ".TP-WHEN-FALLING", Boolean.TRUE);
            plugin.getWorlds().set("WORLDS." +worldName+ ".VOID-POSITION", -5);
            plugin.getWorlds().set("WORLDS." +worldName+ ".WORLD", "&a"+worldName+"");
            plugin.getWorlds().set("WORLDS." +worldName+ ".MATERIAL", "STONE");
            plugin.getWorlds().set("WORLDS." +worldName + ".SPAWN", VOManager.setLocation(worldName));
            plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.TYPE", "CONSOLE");

            final List<String> listCommands = plugin.getWorlds().getStringList("WORLDS." + worldName + ".COMMANDS.LIST");
            listCommands.add("tell {Player} &cThe lobby is not over there!");
            plugin.getWorlds().set("WORLDS." + worldName + ".COMMANDS.LIST", listCommands);

            plugin.files.saveFile("worlds.yml");
            if (event.isAsynchronous()) {
                VOManager.syncRunTask(() -> {
                    VOHome.getInventory(player, 1);
                    XSound.play(player.getLocation(), plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CREATE_WORLD_MENU"));
                });
            } else {
                VOHome.getInventory(player, 1);
                XSound.play(player.getLocation(), plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CREATE_WORLD_MENU"));
            }
            plugin.chat.clear();
            Bukkit.getServer().getScheduler().cancelTask(announce);
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
                    plugin.getCreateBlockY(player.getUniqueId().toString(), event.getMessage());
                    plugin.getWorlds().set("WORLDS." +player.getWorld().getName()+ ".VOID-POSITION", Integer.parseInt(event.getMessage()));
                    plugin.files.saveFile("worlds.yml");
                    if (event.isAsynchronous()) {
                        VOManager.syncRunTask(() -> VOHome.getInventory(player, 1));
                    } else {
                        VOHome.getInventory(player, 1);
                    }
                    Bukkit.getServer().getScheduler().cancelTask(getBlockX);
                    plugin.chat_get.clear();
                    sendTitles.clearTitle(player);
                } else {
                    XSound.play(player.getLocation(), plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CHANGE_Y_LESS_ZERO"));
                    addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_LESS_ZERO"));
                }
            } catch (NumberFormatException e) {
                XSound.play(player.getLocation(), plugin.getConfig().getString("ADMIN-CONFIG.SOUNDS.CHANGE_Y_NO_WORDS"));
                addColor.sendMessage(player, plugin.getFileTranslations().getString("MESSAGE_NO_WORDS").replace("{Word}", event.getMessage()));
            }
        }
    }

    @EventHandler
    public void getChatLeave(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(getBlockX);
        plugin.chat_get.clear();
        Bukkit.getServer().getScheduler().cancelTask(announce);
        plugin.chat.clear();
        Bukkit.getServer().getScheduler().cancelTask(create);
        plugin.create_world.clear();
    }
}
