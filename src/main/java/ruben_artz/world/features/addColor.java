package ruben_artz.world.features;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class addColor {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);
    private static final LegacyComponentSerializer unusualHexSerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    public static void sendMessage(Player player, String message) {
        Audience audience = plugin.getAudiences(player);
        audience.sendMessage(addColors(player, message));
    }

    public static @NotNull Component addColors(Player player, String input) {
        return VOManager.setPlaceholders(player, MiniMessage.miniMessage().deserialize(color(input)));
    }
    public static @NotNull Component addColors(String input) {
        return MiniMessage.miniMessage().deserialize(color(input));
    }

    public static String setColors(String input) {
        if ((input == null) || (input.isEmpty())) return input;
        final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(input);
        return unusualHexSerializer.serialize(component);
    }

    public static List<String> setColors(List<String> input) {
        if ((input == null) || (input.isEmpty())) return input;
        return input.stream().map(addColor::setColors).collect(Collectors.toList());
    }

    private static final ImmutableMap<String, String> colorReplacements = new ImmutableMap.Builder<String, String>()
            .put("0", "<black>")
            .put("1", "<dark_blue>")
            .put("2", "<dark_green>")
            .put("3", "<dark_aqua>")
            .put("4", "<dark_red>")
            .put("5", "<dark_purple>")
            .put("6", "<gold>")
            .put("7", "<gray>")
            .put("8", "<dark_gray>")
            .put("9", "<blue>")
            .put("a", "<green>")
            .put("b", "<aqua>")
            .put("c", "<red>")
            .put("d", "<light_purple>")
            .put("e", "<yellow>")
            .put("f", "<white>")
            .put("k", "<magic>")
            .put("l", "<bold>")
            .put("m", "<strikethrough>")
            .put("n", "<underlined>")
            .put("o", "<italic>")
            .put("r", "<reset>")
            .build();

    private static String color(String msg) {
        for(Map.Entry<String, String> entry : colorReplacements.entrySet()) {
            String legacy = entry.getKey();
            String mini = entry.getValue();
            msg = msg.replaceAll(Matcher.quoteReplacement("&"+legacy), Matcher.quoteReplacement(mini));
            msg = msg.replaceAll(Matcher.quoteReplacement(((char)0x00b7)+legacy), Matcher.quoteReplacement(mini));
        }
        return msg;
    }

    public enum ColorCode {
        COLOR_RESET("\u001B[0m"),
        COLOR_BLACK("\u001B[30m"),
        COLOR_RED("\u001B[31m"),
        COLOR_GREEN("\u001B[32m"),
        COLOR_YELLOW("\u001B[33m"),
        COLOR_BLUE("\u001B[34m"),
        COLOR_PURPLE("\u001B[35m"),
        COLOR_CYAN("\u001B[36m"),
        COLOR_WHITE("\u001B[37m");

        public final String code;

        ColorCode(String code) {
            this.code = code;
        }

        public static String colorizeConsole(ColorCode color, Object message) {
            return color.code + message + ColorCode.COLOR_RESET.code;
        }
    }
}
