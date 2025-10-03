package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.CatEntityVariationSetter;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;

import java.util.List;

public class CatCustomizations {
    public static List<AppearanceCustomization<CatEntity>> getCatCustomizations(CatEntity entity) {
        List<AppearanceCustomization<CatEntity>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);
        customizations.add(new CatVariantCustomization(entity));
        customizations.add(new CatCollarCustomization(entity.getCollarColor()));
        return customizations;
    }

    private record CatVariantCustomization(Variant variant)
            implements AppearanceCustomization<CatEntity> {
        private enum Variant {
            TABBY(CatVariants.TABBY),
            BLACK(CatVariants.BLACK),
            RED(CatVariants.RED),
            SIAMESE(CatVariants.SIAMESE),
            BRITISH_SHORTHAIR(CatVariants.BRITISH_SHORTHAIR),
            CALICO(CatVariants.CALICO),
            PERSIAN(CatVariants.PERSIAN),
            RAGDOLL(CatVariants.RAGDOLL),
            WHITE(CatVariants.WHITE),
            JELLIE(CatVariants.JELLIE),
            ALL_BLACK(CatVariants.ALL_BLACK);
            
            private final RegistryKey<CatVariant> key;
            private Variant(RegistryKey<CatVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<CatVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private CatVariantCustomization(CatEntity cat) {
            this(Variant.of(cat.getVariant().getKey().orElseThrow()));
        }

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
                case TABBY -> Items.BROWN_DYE;
                case BLACK, ALL_BLACK -> Items.BLACK_DYE;
                case RED, CALICO -> Items.ORANGE_DYE;
                case SIAMESE, PERSIAN -> Items.YELLOW_DYE;
                case BRITISH_SHORTHAIR, RAGDOLL -> Items.LIGHT_GRAY_DYE;
                case WHITE -> Items.WHITE_DYE;
                case JELLIE -> Items.GRAY_DYE;
            };
        }

        @Override
        public AppearanceCustomization<CatEntity> setNext(CatEntity shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((CatEntityVariationSetter) shopkeeper).invokeSetVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new CatVariantCustomization(next);
        }
    }
    
    private record CatCollarCustomization(DyeColor color) implements AppearanceCustomization<CatEntity> {

        @Override
        public String customizationDescription() {
            return "Collar Color";
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
        public AppearanceCustomization<CatEntity> setNext(CatEntity shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            ((CatEntityVariationSetter) shopkeeper).invokeSetCollarColor(next);
            return new CatCollarCustomization(next);
        }
    }
}
