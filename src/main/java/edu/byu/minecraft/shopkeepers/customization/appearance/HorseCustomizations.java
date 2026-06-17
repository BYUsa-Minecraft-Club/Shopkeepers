package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.HorseEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Markings;
import net.minecraft.world.entity.animal.equine.Variant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class HorseCustomizations {

    public static List<AppearanceCustomization<Horse>> getHorseCustomizations(Horse horse) {
        List<AppearanceCustomization<Horse>> customizations = new ArrayList<>();
        customizations.add(new HorseBaseColorCustomization(horse.getVariant()));
        customizations.add(new HorseMarkingCustomization(horse.getMarkings()));
        return customizations;
    }

    private record HorseBaseColorCustomization(Variant baseColor)
            implements AppearanceCustomization<Horse> {

        @Override
        public String customizationDescription() {
            return "Base Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(baseColor.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (baseColor) {
                case WHITE -> Items.WHITE_DYE;
                case CREAMY -> Items.ORANGE_DYE;
                case CHESTNUT -> Items.RED_DYE;
                case BROWN -> Items.BROWN_DYE;
                case BLACK -> Items.BLACK_DYE;
                case GRAY -> Items.GRAY_DYE;
                case DARK_BROWN -> Items.DARK_OAK_LOG;
            };
        }

        @Override
        public AppearanceCustomization<Horse> setNext(Horse shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(baseColor, Variant.values());
            ((HorseEntityVariantSetter) shopkeeper).invokeSetVariant(next, shopkeeper.getMarkings());
            return new HorseBaseColorCustomization(next);
        }
    }

    private record HorseMarkingCustomization(Markings marking)
            implements AppearanceCustomization<Horse> {

        @Override
        public String customizationDescription() {
            return "Markings";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(marking.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (marking) {
                case NONE -> Items.BARRIER;
                case WHITE -> Items.LEATHER_BOOTS;
                case WHITE_FIELD -> Items.WHITE_DYE;
                case WHITE_DOTS -> Items.BONE_MEAL;
                case BLACK_DOTS -> Items.BLACK_DYE;
            };
        }

        @Override
        public AppearanceCustomization<Horse> setNext(Horse shopkeeper) {
            Markings next = CustomizationUtils.nextEnum(marking, Markings.values());
            ((HorseEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.getVariant(), next);
            return new HorseMarkingCustomization(next);
        }
    }
}
