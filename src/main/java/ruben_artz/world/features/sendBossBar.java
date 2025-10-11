package ruben_artz.world.features;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.UtilityFunctions;
import ruben_artz.world.utils.addColor;

import java.util.Objects;

public class sendBossBar {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void sendBoss(final Player player, FileConfiguration file, String pathColor, String pathMessage) {
        Audience audience = plugin.getAudiences(player);
        pathMessage = Objects.requireNonNull(pathMessage)
                .replace("{Player}", player.getName())
                .replace("{Address}", String.valueOf(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()))
                .replace("{Uuid}", player.getUniqueId().toString());
        final Component name = addColor.addColors(player, pathMessage);
        final BossBar bossColor = BossBar.bossBar(
                name, 1,
                getBarColor(file, pathColor),
                BossBar.Overlay.PROGRESS);

        audience.showBossBar(bossColor);

        for (int i = 1; i <= 6; i++) {
            UtilityFunctions.runTaskLater(20 * i, () -> {
                if (bossColor.progress() >= 0.2f) {
                    bossColor.progress(bossColor.progress() - 0.2f);
                } else {
                    audience.hideBossBar(bossColor);
                }
            });
        }
    }

    private static BossBar.Color getBarColor(FileConfiguration file, String pathFile) {
        if (file.contains(pathFile)) {
            String color = file.getString(pathFile);
            if (color != null && color.equalsIgnoreCase("PINK")) return BossBar.Color.PINK;
            if (color != null && color.equalsIgnoreCase("YELLOW")) return BossBar.Color.YELLOW;
            if (color != null && color.equalsIgnoreCase("GREEN")) return BossBar.Color.GREEN;
            if (color != null && color.equalsIgnoreCase("BLUE")) return BossBar.Color.BLUE;
            if (color != null && color.equalsIgnoreCase("RED")) return BossBar.Color.RED;
            if (color != null && color.equalsIgnoreCase("WHITE")) return BossBar.Color.WHITE;
            if (color != null) {
                return color.equalsIgnoreCase("PURPLE") ? BossBar.Color.PURPLE : BossBar.Color.PINK;
            }
        }
        return BossBar.Color.PINK;
    }
}
