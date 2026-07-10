package edu.byu.minecraft.shopkeepers.customization;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
            case WHITE -> Items.DYE.white();
            case ORANGE -> Items.DYE.orange();
            case MAGENTA -> Items.DYE.magenta();
            case LIGHT_BLUE -> Items.DYE.lightBlue();
            case YELLOW -> Items.DYE.yellow();
            case LIME -> Items.DYE.lime();
            case PINK -> Items.DYE.pink();
            case GRAY -> Items.DYE.gray();
            case LIGHT_GRAY -> Items.DYE.lightGray();
            case CYAN -> Items.DYE.cyan();
            case PURPLE -> Items.DYE.purple();
            case BLUE -> Items.DYE.blue();
            case BROWN -> Items.DYE.brown();
            case GREEN -> Items.DYE.green();
            case RED -> Items.DYE.red();
            case BLACK -> Items.DYE.black();
        };
    }
}
