package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.HorseEntityVariantSetter;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class HorseCustomizations {

    public static List<ShopkeeperCustomization<HorseEntity>> getHorseCustomizations(HorseEntity horse) {
        List<ShopkeeperCustomization<HorseEntity>> customizations = new ArrayList<>();
        customizations.add(new HorseBaseColorCustomization(horse.getHorseColor()));
        customizations.add(new HorseMarkingCustomization(horse.getMarking()));
        return customizations;
    }

    private record HorseBaseColorCustomization(HorseColor baseColor)
            implements ShopkeeperCustomization<HorseEntity> {

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
        public ShopkeeperCustomization<HorseEntity> setNext(HorseEntity shopkeeper) {
            HorseColor next = CustomizationUtils.nextAlphabetically(shopkeeper.getHorseColor());
            ((HorseEntityVariantSetter) shopkeeper).invokeSetVariant(next, shopkeeper.getMarking());
            return new HorseBaseColorCustomization(next);
        }
    }

    private record HorseMarkingCustomization(HorseMarking marking)
            implements ShopkeeperCustomization<HorseEntity> {

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
        public ShopkeeperCustomization<HorseEntity> setNext(HorseEntity shopkeeper) {
            HorseMarking next = CustomizationUtils.nextAlphabetically(shopkeeper.getMarking());
            ((HorseEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.getHorseColor(), next);
            return new HorseMarkingCustomization(next);
        }
    }
}
