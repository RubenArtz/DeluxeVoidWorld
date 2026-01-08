package artzstudio.dev.deluxevoid.firework;

import org.bukkit.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ColorUtils {

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
                                throw new RuntimeException();
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
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        return color;
    }
}