/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.utils;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Matcher;

public class addColor {
    private static final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);
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

    public static void sendMessage(Player player, String message) {
        Audience audience = plugin.getAudiences(player);
        audience.sendMessage(addColors(player, message));
    }

    public static @NotNull Component addColors(Player player, String input) {
        return UtilityFunctions.setPlaceholders(player, MiniMessage.miniMessage().deserialize(color(input)));
    }

    public static @NotNull Component addColors(String input) {
        return MiniMessage.miniMessage().deserialize(color(input));
    }

    public static String setColors(String input) {
        Component component = addColors(input);

        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    private static String color(String msg) {
        for (Map.Entry<String, String> entry : colorReplacements.entrySet()) {
            String legacy = entry.getKey();
            String mini = entry.getValue();
            msg = msg.replaceAll(Matcher.quoteReplacement("&" + legacy), Matcher.quoteReplacement(mini));
            msg = msg.replaceAll(Matcher.quoteReplacement(((char) 0x00b7) + legacy), Matcher.quoteReplacement(mini));
        }
        return msg;
    }
}