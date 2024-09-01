package ruben_artz.world.features;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.addColor;

import java.time.Duration;
import java.util.Objects;

public class sendTitles {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subTitle) {
        Audience audience = plugin.getAudiences(player);

        title = Objects.requireNonNull(title)
                .replace("{Player}", player.getName())
                .replace("{Address}", String.valueOf(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()))
                .replace("{Uuid}", player.getUniqueId().toString());
        subTitle = Objects.requireNonNull(subTitle)
                .replace("{Player}", player.getName())
                .replace("{Address}", String.valueOf(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()))
                .replace("{Uuid}", player.getUniqueId().toString());
        Component text_title = addColor.addColors(player, title);
        Component text_subTitle = addColor.addColors(player, subTitle);

        Title createTitle = Title.title(text_title, text_subTitle, Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofMillis(fadeOut)));

        audience.showTitle(createTitle);
    }

    public static void clearTitle(Player player) {
        Audience audience = plugin.getAudiences(player);
        audience.clearTitle();
    }
}