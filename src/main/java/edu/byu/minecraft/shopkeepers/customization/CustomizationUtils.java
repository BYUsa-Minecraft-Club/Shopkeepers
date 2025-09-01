package edu.byu.minecraft.shopkeepers.customization;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

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

    static <E extends Enum<E>> E nextAlphabetically(E e) {
        E[] values = (E[]) e.getClass().getEnumConstants();
        Arrays.sort(values, Comparator.comparing(Enum::name));
        int index = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == e) index = i;
        }
        return values[(index + 1) % values.length];
    }
}
