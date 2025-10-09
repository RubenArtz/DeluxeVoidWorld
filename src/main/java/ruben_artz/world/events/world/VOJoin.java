package ruben_artz.world.events.world;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.events.world.antibot.preventAttacks;
import ruben_artz.world.launcher.Launcher;
import ruben_artz.world.utils.CrossPlatformUtils;
import ruben_artz.world.utils.Updater;
import ruben_artz.world.utils.addColor;

public class VOJoin implements Listener {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void verifyPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CrossPlatformUtils.runTaskAsynchronously(() -> {
            /*
             * Add data if the player does not exist!
             */
            try {
                if (Launcher.getCache().ifNotExists(player.getUniqueId())) {
                    if (preventAttacks.isAttacking()) return;
                    Launcher.getCache().addInformation(player.getUniqueId());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Launcher.getCache().updatePlayer(player);
        });
    }

    @EventHandler
    public void onAuthor(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if ("Ruben_Artz".equals(player.getName())) {
            CrossPlatformUtils.runTask(() -> {
                Updater.getUpdater();
                player.sendMessage(addColor.setColors("&8« » =========== &e✯ &9&lDeluxe Void World &e✯ &8=========== « »"));
                player.sendMessage(addColor.setColors("&f"));
                CrossPlatformUtils.sendTextComponent(player,
                        "&fVersion: &av" + plugin.getVersion(),
                        ClickEvent.Action.OPEN_URL,
                        "https://api.spigotmc.org/legacy/update.php?resource=86993",
                        HoverEvent.Action.SHOW_TEXT,
                        "&fÚltima versión del plugin: &a" + plugin.getLatestversion());
                player.sendMessage(addColor.setColors("&fAutor: &a" + plugin.getDescription().getAuthors()));
                player.sendMessage(addColor.setColors("&fMapas: &a" + Launcher.getNumberWorlds()));
                player.sendMessage(addColor.setColors("&fPlugin: &a" + plugin.getPrefix()));
                player.sendMessage(addColor.setColors("&8================================================="));
            });
        }
    }
}
