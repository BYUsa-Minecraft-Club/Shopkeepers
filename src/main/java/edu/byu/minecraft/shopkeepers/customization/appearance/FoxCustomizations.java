package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.FoxEntityVariationSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class FoxCustomizations {

    public static List<AppearanceCustomization<Fox>> getFoxCustomizations(Fox fox) {
        List<AppearanceCustomization<Fox>> customizations = new ArrayList<>();
        customizations.add(new FoxVariantCustomization(fox.getVariant()));
        customizations.add(FoxPositionCustomization.forFox(fox));
        return customizations;
    }

    private record FoxVariantCustomization(Fox.Variant variant) implements AppearanceCustomization<Fox> {

        @Override
            public String customizationDescription() {
                return "Variant";
            }

            @Override
            public String currentDescription() {
                return CustomizationUtils.capitalize(variant.name());
            }

            @Override
            public Item getCurrentRepresentationItem() {
                return switch (variant) {
                    case RED -> Items.ORANGE_DYE;
                    case SNOW -> Items.WHITE_DYE;
                };
            }

            @Override
            public AppearanceCustomization<Fox> setNext(Fox shopkeeper) {
                Fox.Variant next = CustomizationUtils.nextEnum(variant, Fox.Variant.values());
                ((FoxEntityVariationSetter) shopkeeper).invokeSetVariant(next);
                return new FoxVariantCustomization(next);
            }
        }

    private record FoxPositionCustomization(FoxPosition position) implements AppearanceCustomization<Fox> {
        private enum FoxPosition {
            STANDING, SNEAKING, SITTING, SLEEPING
        }

        private static FoxPositionCustomization forFox(Fox fox) {
            if(fox.isSleeping()) return new FoxPositionCustomization(FoxPosition.SLEEPING);
            if(fox.isSitting()) return new FoxPositionCustomization(FoxPosition.SITTING);
            if(fox.isCrouching()) return new FoxPositionCustomization(FoxPosition.SNEAKING);
            return new FoxPositionCustomization(FoxPosition.STANDING);
        }

        @Override
        public String customizationDescription() {
            return "Fox Pose";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(position.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (position) {
                case STANDING -> Items.ACACIA_PLANKS;
                case SNEAKING -> Items.ACACIA_SLAB;
                case SITTING -> Items.SADDLE;
                case SLEEPING -> Items.WHITE_BED;
            };
        }

        @Override
        public AppearanceCustomization<Fox> setNext(Fox shopkeeper) {
            FoxPosition next = CustomizationUtils.nextEnum(position(), FoxPosition.values());
            ((FoxEntityVariationSetter) shopkeeper).invokeSetSleeping(next == FoxPosition.SLEEPING);
            shopkeeper.setSitting(next == FoxPosition.SITTING);
            shopkeeper.setIsCrouching(next == FoxPosition.SNEAKING);
            return new FoxPositionCustomization(next);
        }
    }
}
