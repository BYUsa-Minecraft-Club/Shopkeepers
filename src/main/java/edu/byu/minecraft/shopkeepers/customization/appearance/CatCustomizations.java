package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.CatEntityVariationSetter;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.feline.CatVariants;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CatCustomizations {
    public static List<AppearanceCustomization<Cat>> getCatCustomizations(Cat entity) {
        List<AppearanceCustomization<Cat>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);
        customizations.add(new CatVariantCustomization(entity));
        customizations.add(new CatCollarCustomization(entity.getCollarColor()));
        return customizations;
    }

    private record CatVariantCustomization(Variant variant)
            implements AppearanceCustomization<Cat> {
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
            
            private final ResourceKey<CatVariant> key;
            private Variant(ResourceKey<CatVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<CatVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private CatVariantCustomization(Cat cat) {
            this(Variant.of(cat.getVariant().unwrapKey().orElseThrow()));
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
                case TABBY -> Items.DYE.brown();
                case BLACK, ALL_BLACK -> Items.DYE.black();
                case RED, CALICO -> Items.DYE.orange();
                case SIAMESE, PERSIAN -> Items.DYE.yellow();
                case BRITISH_SHORTHAIR, RAGDOLL -> Items.DYE.lightGray();
                case WHITE -> Items.DYE.white();
                case JELLIE -> Items.DYE.gray();
            };
        }

        @Override
        public AppearanceCustomization<Cat> setNext(Cat shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((CatEntityVariationSetter) shopkeeper).invokeSetVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new CatVariantCustomization(next);
        }
    }
    
    private record CatCollarCustomization(DyeColor color) implements AppearanceCustomization<Cat> {

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
        public AppearanceCustomization<Cat> setNext(Cat shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            ((CatEntityVariationSetter) shopkeeper).invokeSetCollarColor(next);
            return new CatCollarCustomization(next);
        }
    }
}
