package edu.byu.minecraft.shopkeepers.customization.appearance;


import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.TropicalFishEntityVariationSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TropicalFishCustomizations {
    public static List<AppearanceCustomization<TropicalFish>> getTropicalFishCustomizations(TropicalFish entity) {
        List<AppearanceCustomization<TropicalFish>> customizations = new ArrayList<>();
        customizations.add(new TropicalFishPatternCustomization(entity.getPattern()));
        customizations.add(new TropicalFishBaseColorCustomization(entity.getBaseColor()));
        customizations.add(new TropicalFishPatternColorCustomization(entity.getPatternColor()));
        return customizations;
    }

    private record TropicalFishPatternCustomization(TropicalFish.Pattern pattern)
            implements AppearanceCustomization<TropicalFish> {

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
        public AppearanceCustomization<TropicalFish> setNext(TropicalFish shopkeeper) {
            var next = CustomizationUtils.nextEnum(pattern, TropicalFish.Pattern.values());
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetVariety(next);
            return new TropicalFishPatternCustomization(next);
        }
    }
    
    private record TropicalFishBaseColorCustomization(DyeColor color) implements
            AppearanceCustomization<TropicalFish> {

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
        public AppearanceCustomization<TropicalFish> setNext(TropicalFish shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetBaseColor(next);
            return new TropicalFishBaseColorCustomization(next);
        }
    }

    private record TropicalFishPatternColorCustomization(DyeColor color) implements
            AppearanceCustomization<TropicalFish> {

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
        public AppearanceCustomization<TropicalFish> setNext(TropicalFish shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            ((TropicalFishEntityVariationSetter)shopkeeper).invokeSetPatternColor(next);
            return new TropicalFishPatternColorCustomization(next);
        }
    }

}
