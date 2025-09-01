package edu.byu.minecraft.shopkeepers.customization;

public class CustomizationUtils {

    static String capitalize(String s) {
        String[] split = s.split("[_ ]");
        for (int i = 0; i < split.length; i++) {
            split[i] = capitalizeSingle(split[i]);
        }
        return String.join(" ", split);
    }

    private static String capitalizeSingle(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
