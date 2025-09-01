package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

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

    static Item getDyeItem(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }
}
