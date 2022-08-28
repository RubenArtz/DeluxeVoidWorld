package ruben_artz.world.world;

import org.bukkit.Bukkit;
import ruben_artz.world.main.VOMain;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@SuppressWarnings({"deprecation", "ConstantConditions"})
public class VOUpdater {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);

    public static int updater;

    public static void setEnabled() {
        if (plugin.getConfig().getString("ADMIN-CONFIG.CHECK_UPDATE").contains("false")) {
            shutdown();
        } else {
            launch();
        }
    }

    public static void launch() {
        updater = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, VOUpdater::getUpdater, 0L, 20L * 18000);
    }
    public static void shutdown() {
        Bukkit.getScheduler().cancelTask(updater);
    }

    public static void getUpdater() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=86993").openConnection();
            int time_out = 1250;
            connection.setConnectTimeout(time_out);
            connection.setReadTimeout(time_out);
            plugin.latestversion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            if ((plugin.latestversion.length() <= 7) && (!plugin.getVersion().equals(plugin.latestversion))) {
                plugin.sendConsole( "&8--------------------------------------------------------------------------------------");
                plugin.sendConsole( ""+plugin.prefix+"&fYou have an old version of the &eDeluxe Void World &fplugin.");
                plugin.sendConsole( ""+plugin.prefix+"&fPlease download the latest &e"+plugin.getLatestVersion()+" &fversion.");
                plugin.sendConsole( "&8--------------------------------------------------------------------------------------");
            }
        } catch (Exception exception) {
            plugin.sendConsole(""+plugin.prefix+"&6We have problems establishing connection with spigotmc.org");
        }
    }
}
