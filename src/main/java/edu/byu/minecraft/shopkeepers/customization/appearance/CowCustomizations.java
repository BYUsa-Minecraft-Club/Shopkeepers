package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.cow.CowVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CowCustomizations {
    public static List<AppearanceCustomization<Cow>> getCowCustomizations(Cow entity) {
        List<AppearanceCustomization<Cow>> customizations = new ArrayList<>();
        customizations.add(new CowVariantCustomization(entity));
        return customizations;
    }

    private record CowVariantCustomization(Variant variant)
            implements AppearanceCustomization<Cow> {
        private enum Variant {
            TEMPERATE(CowVariants.TEMPERATE),
            WARM(CowVariants.WARM),
            COLD(CowVariants.COLD);
            
            private final ResourceKey<CowVariant> key;
            private Variant(ResourceKey<CowVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<CowVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private CowVariantCustomization(Cow cow) {
            this(Variant.of(cow.getVariant().unwrapKey().orElseThrow()));
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
                case TEMPERATE -> Items.BROWN_DYE;
                case WARM -> Items.RED_DYE;
                case COLD -> Items.ORANGE_DYE;
            };
        }

        @Override
        public AppearanceCustomization<Cow> setNext(Cow shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            shopkeeper.setVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new CowVariantCustomization(next);
        }
    }

}
