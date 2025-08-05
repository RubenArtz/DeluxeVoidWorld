package ruben_artz.world.utils;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import ruben_artz.world.DeluxeVoidWorld;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@SuppressWarnings({"ConstantConditions"})
public class Updater {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static MyScheduledTask updater;

    public static void setEnabled() {
        if (plugin.getConfig().getString("ADMIN-CONFIG.CHECK_UPDATE").contains("false")) {
            shutdown();
        } else {
            launch();
        }
    }

    public static void launch() {
        updater = DeluxeVoidWorld.getScheduler().runTaskTimerAsynchronously(Updater::getUpdater, 0L, 20L * 18000);
    }

    public static void shutdown() {
        updater.cancel();
    }

    public static void getUpdater() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=86993").openConnection();
            int time_out = 1250;
            connection.setConnectTimeout(time_out);
            connection.setReadTimeout(time_out);
            plugin.latestversion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            if ((plugin.latestversion.length() <= 7) && (!plugin.getVersion().equals(plugin.latestversion))) {
                plugin.sendConsole("&8--------------------------------------------------------------------------------------");
                plugin.sendConsole(plugin.getPrefix() + "&fYou have an old version of the &eDeluxe Void World &fplugin.");
                plugin.sendConsole(plugin.getPrefix() + "&fPlease download the latest &e" + plugin.getLatestversion() + " &fversion.");
                plugin.sendConsole("&8--------------------------------------------------------------------------------------");
            }
        } catch (Exception ignored) {
        }
    }
}