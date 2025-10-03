package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.Arrays;
import java.util.Comparator;

public class CustomizationUtils {
    public static enum DyeColorWithNone {
        NONE, WHITE, LIGHT_GRAY, GRAY, BLACK, PINK, RED, ORANGE, BROWN, YELLOW, LIME, GREEN, CYAN, LIGHT_BLUE, BLUE,
        PURPLE, MAGENTA
    }
    
    private static final DyeColor[] ORDERED_DYE_COLORS =
            new DyeColor[]{DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.PINK,
                    DyeColor.RED, DyeColor.ORANGE, DyeColor.BROWN, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN,
                    DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA};

    public static String capitalize(String s) {
        String[] split = s.split("[_ ]");
        for (int i = 0; i < split.length; i++) {
            split[i] = capitalizeSingle(split[i]);
        }
        return String.join(" ", split);
    }

    private static String capitalizeSingle(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static DyeColor nextInOrder(DyeColor e) {
        int index = 0;
        for (int i = 0; i < ORDERED_DYE_COLORS.length; i++) {
            if (ORDERED_DYE_COLORS[i] == e) index = i;
        }
        return ORDERED_DYE_COLORS[(index + 1) % ORDERED_DYE_COLORS.length];
    }

    public static <E extends Enum<E>> E nextEnum(E e, E[] values) {
        return values[(e.ordinal() + 1) % values.length];
    }

    public static Item getDyeItem(DyeColor dyeColor) {
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
