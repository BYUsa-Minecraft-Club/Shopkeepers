package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.FrogEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class FrogCustomizations {
    public static List<AppearanceCustomization<Frog>> getFrogCustomizations(Frog entity) {
        List<AppearanceCustomization<Frog>> customizations = new ArrayList<>();
        customizations.add(new FrogVariantCustomization(entity));
        return customizations;
    }

    private record FrogVariantCustomization(Variant variant)
            implements AppearanceCustomization<Frog> {
        private enum Variant {
            TEMPERATE(FrogVariants.TEMPERATE),
            WARM(FrogVariants.WARM),
            COLD(FrogVariants.COLD);
            
            private final ResourceKey<FrogVariant> key;
            private Variant(ResourceKey<FrogVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<FrogVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private FrogVariantCustomization(Frog frog) {
            this(Variant.of(frog.getVariant().unwrapKey().orElseThrow()));
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
        public AppearanceCustomization<Frog> setNext(Frog shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((FrogEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new FrogVariantCustomization(next);
        }
    }
    
}
