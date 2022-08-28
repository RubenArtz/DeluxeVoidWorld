package ruben_artz.world.firework;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ColorUtils {
    public static String addColors(String input) {
        if ((input == null) || (input.isEmpty())) {
            return input;
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

    public static List<String> addColors(List<String> input) {
        if ((input == null) || (input.isEmpty())) {
            return input;
        }
        for (int i = 0; i < input.size(); i++) {
            input.set(i, addColors((String) input.get(i)));
        }
        return input;
    }

    public static Color parseColor(String input) {
        Color color = null;
        String[] split = input.split(" ");
        if (split.length > 2) {
            try {
                int red = Integer.parseInt(split[0]);
                int green = Integer.parseInt(split[1]);
                int blue = Integer.parseInt(split[2]);
                color = Color.fromRGB(red, green, blue);
            } catch (NumberFormatException numberFormatException) {
                Field[] fields = Color.class.getFields();
                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers()) && field.getType() == Color.class) {
                        if (field.getName().equalsIgnoreCase(input)) {
                            try {
                                return (Color) field.get(null);
                            } catch (IllegalArgumentException | IllegalAccessException illegalArgumentException) {
                                illegalArgumentException.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            Field[] fields = Color.class.getFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType() == Color.class) {
                    if (field.getName().equalsIgnoreCase(input)) {
                        try {
                            return (Color) field.get(null);
                        } catch (IllegalArgumentException | IllegalAccessException illegalArgumentException) {
                            illegalArgumentException.printStackTrace();
                        }
                    }
                }
            }
        }
        return color;
    }
}