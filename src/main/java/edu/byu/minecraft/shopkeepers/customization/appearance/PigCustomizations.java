package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.PigEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.pig.PigVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PigCustomizations {
    public static List<AppearanceCustomization<Pig>> getPigCustomizations(Pig entity) {
        List<AppearanceCustomization<Pig>> customizations = new ArrayList<>();
        customizations.add(new PigVariantCustomization(entity));
        return customizations;
    }

    private record PigVariantCustomization(Variant variant)
            implements AppearanceCustomization<Pig> {
        private enum Variant {
            TEMPERATE(PigVariants.TEMPERATE),
            WARM(PigVariants.WARM),
            COLD(PigVariants.COLD);
            
            private final ResourceKey<PigVariant> key;
            private Variant(ResourceKey<PigVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<PigVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private PigVariantCustomization(Pig pig) {
            this(Variant.of(pig.getVariant().unwrapKey().orElseThrow()));
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
                case TEMPERATE -> Items.DYE.pink();
                case WARM -> Items.DYE.orange();
                case COLD -> Items.DYE.yellow();
            };
        }

        @Override
        public AppearanceCustomization<Pig> setNext(Pig shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((PigEntityVariantSetter) shopkeeper).invokeSetVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new PigVariantCustomization(next);
        }
    }

}
