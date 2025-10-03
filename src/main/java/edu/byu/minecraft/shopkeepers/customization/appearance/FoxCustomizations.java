package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.FoxEntityVariationSetter;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class FoxCustomizations {

    public static List<AppearanceCustomization<FoxEntity>> getFoxCustomizations(FoxEntity fox) {
        List<AppearanceCustomization<FoxEntity>> customizations = new ArrayList<>();
        customizations.add(new FoxVariantCustomization(fox.getVariant()));
        customizations.add(FoxPositionCustomization.forFox(fox));
        return customizations;
    }

    private record FoxVariantCustomization(FoxEntity.Variant variant) implements AppearanceCustomization<FoxEntity> {

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
            public AppearanceCustomization<FoxEntity> setNext(FoxEntity shopkeeper) {
                FoxEntity.Variant next = CustomizationUtils.nextEnum(variant, FoxEntity.Variant.values());
                ((FoxEntityVariationSetter) shopkeeper).invokeSetVariant(next);
                return new FoxVariantCustomization(next);
            }
        }

    private record FoxPositionCustomization(FoxPosition position) implements AppearanceCustomization<FoxEntity> {
        private enum FoxPosition {
            STANDING, SNEAKING, SITTING, SLEEPING
        }

        private static FoxPositionCustomization forFox(FoxEntity fox) {
            if(fox.isSleeping()) return new FoxPositionCustomization(FoxPosition.SLEEPING);
            if(fox.isSitting()) return new FoxPositionCustomization(FoxPosition.SITTING);
            if(fox.isInSneakingPose()) return new FoxPositionCustomization(FoxPosition.SNEAKING);
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
        public AppearanceCustomization<FoxEntity> setNext(FoxEntity shopkeeper) {
            FoxPosition next = CustomizationUtils.nextEnum(position(), FoxPosition.values());
            ((FoxEntityVariationSetter) shopkeeper).invokeSetSleeping(next == FoxPosition.SLEEPING);
            shopkeeper.setSitting(next == FoxPosition.SITTING);
            shopkeeper.setCrouching(next == FoxPosition.SNEAKING);
            return new FoxPositionCustomization(next);
        }
    }
}
