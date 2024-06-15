package ruben_artz.world.features;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ruben_artz.world.DeluxeVoidWorld;

import java.util.Objects;

public class sendActionbar {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
    public static void sendActionBar(Player player, String message) {
        Audience audience = plugin.getAudiences(player);

        message = Objects.requireNonNull(message)
                .replace("{Player}", player.getName())
                .replace("{Address}", String.valueOf(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()))
                .replace("{Uuid}", player.getUniqueId().toString());
        Component text = addColor.addColors(player, message);

        audience.sendActionBar(text);
    }
}
