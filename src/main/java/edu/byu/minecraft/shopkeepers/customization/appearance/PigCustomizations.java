package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.PigEntityVariantSetter;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PigVariant;
import net.minecraft.entity.passive.PigVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public class PigCustomizations {
    public static List<AppearanceCustomization<PigEntity>> getPigCustomizations(PigEntity entity) {
        List<AppearanceCustomization<PigEntity>> customizations = new ArrayList<>();
        customizations.add(new PigVariantCustomization(entity));
        return customizations;
    }

    private record PigVariantCustomization(Variant variant)
            implements AppearanceCustomization<PigEntity> {
        private enum Variant {
            TEMPERATE(PigVariants.TEMPERATE),
            WARM(PigVariants.WARM),
            COLD(PigVariants.COLD);
            
            private final RegistryKey<PigVariant> key;
            private Variant(RegistryKey<PigVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<PigVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private PigVariantCustomization(PigEntity pig) {
            this(Variant.of(pig.getVariant().getKey().orElseThrow()));
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
                case TEMPERATE -> Items.PINK_DYE;
                case WARM -> Items.ORANGE_DYE;
                case COLD -> Items.YELLOW_DYE;
            };
        }

        @Override
        public AppearanceCustomization<PigEntity> setNext(PigEntity shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((PigEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new PigVariantCustomization(next);
        }
    }

}
