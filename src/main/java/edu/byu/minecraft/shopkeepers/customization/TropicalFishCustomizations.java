package edu.byu.minecraft.shopkeepers.customization;


import edu.byu.minecraft.shopkeepers.mixin.invoker.TropicalFishEntityVariationSetter;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class TropicalFishCustomizations {
    public static List<ShopkeeperCustomization<TropicalFishEntity>> getTropicalFishCustomizations(TropicalFishEntity entity) {
        List<ShopkeeperCustomization<TropicalFishEntity>> customizations = new ArrayList<>();
        customizations.add(new TropicalFishPatternCustomization(entity.getVariety()));
        customizations.add(new TropicalFishBaseColorCustomization(entity.getBaseColor()));
        customizations.add(new TropicalFishPatternColorCustomization(entity.getPatternColor()));
        return customizations;
    }

    private record TropicalFishPatternCustomization(TropicalFishEntity.Pattern pattern)
            implements ShopkeeperCustomization<TropicalFishEntity> {

        @Override
        public String customizationDescription() {
            return "Pattern";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(pattern.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return Items.TROPICAL_FISH_BUCKET;
        }

        @Override
        public ShopkeeperCustomization<TropicalFishEntity> setNext(TropicalFishEntity shopkeeper) {
            var next = CustomizationUtils.nextAlphabetically(pattern, TropicalFishEntity.Pattern.values());
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetVariety(next);
            return new TropicalFishPatternCustomization(next);
        }
    }
    
    private record TropicalFishBaseColorCustomization(DyeColor color) implements ShopkeeperCustomization<TropicalFishEntity> {

        @Override
        public String customizationDescription() {
            return "Base Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(color.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return CustomizationUtils.getDyeItem(color);
        }

        @Override
        public ShopkeeperCustomization<TropicalFishEntity> setNext(TropicalFishEntity shopkeeper) {
            DyeColor next = CustomizationUtils.nextAlphabetically(color, DyeColor.values());
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetBaseColor(next);
            return new TropicalFishBaseColorCustomization(next);
        }
    }

    private record TropicalFishPatternColorCustomization(DyeColor color) implements ShopkeeperCustomization<TropicalFishEntity> {

        @Override
        public String customizationDescription() {
            return "Pattern Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(color.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return CustomizationUtils.getDyeItem(color);
        }

        @Override
        public ShopkeeperCustomization<TropicalFishEntity> setNext(TropicalFishEntity shopkeeper) {
            DyeColor next = CustomizationUtils.nextAlphabetically(color, DyeColor.values());
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetPatternColor(next);
            return new TropicalFishPatternColorCustomization(next);
        }
    }

}
