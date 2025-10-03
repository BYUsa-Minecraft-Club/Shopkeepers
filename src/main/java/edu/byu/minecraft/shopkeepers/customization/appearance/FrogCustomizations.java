package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.FrogEntityVariantSetter;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.FrogVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public class FrogCustomizations {
    public static List<AppearanceCustomization<FrogEntity>> getFrogCustomizations(FrogEntity entity) {
        List<AppearanceCustomization<FrogEntity>> customizations = new ArrayList<>();
        customizations.add(new FrogVariantCustomization(entity));
        return customizations;
    }

    private record FrogVariantCustomization(Variant variant)
            implements AppearanceCustomization<FrogEntity> {
        private enum Variant {
            TEMPERATE(FrogVariants.TEMPERATE),
            WARM(FrogVariants.WARM),
            COLD(FrogVariants.COLD);
            
            private final RegistryKey<FrogVariant> key;
            private Variant(RegistryKey<FrogVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<FrogVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private FrogVariantCustomization(FrogEntity frog) {
            this(Variant.of(frog.getVariant().getKey().orElseThrow()));
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
                case TEMPERATE -> Items.OCHRE_FROGLIGHT;
                case WARM -> Items.PEARLESCENT_FROGLIGHT;
                case COLD -> Items.VERDANT_FROGLIGHT;
            };
        }

        @Override
        public AppearanceCustomization<FrogEntity> setNext(FrogEntity shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((FrogEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new FrogVariantCustomization(next);
        }
    }
    
}
