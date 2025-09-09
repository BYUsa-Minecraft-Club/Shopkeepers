package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.CowVariant;
import net.minecraft.entity.passive.CowVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public class CowCustomizations {
    public static List<AppearanceCustomization<CowEntity>> getCowCustomizations(CowEntity entity) {
        List<AppearanceCustomization<CowEntity>> customizations = new ArrayList<>();
        customizations.add(new CowVariantCustomization(entity));
        return customizations;
    }

    private record CowVariantCustomization(Variant variant)
            implements AppearanceCustomization<CowEntity> {
        private enum Variant {
            TEMPERATE(CowVariants.TEMPERATE),
            WARM(CowVariants.WARM),
            COLD(CowVariants.COLD);
            
            private final RegistryKey<CowVariant> key;
            private Variant(RegistryKey<CowVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<CowVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private CowVariantCustomization(CowEntity cow) {
            this(Variant.of(cow.getVariant().getKey().orElseThrow()));
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
        public AppearanceCustomization<CowEntity> setNext(CowEntity shopkeeper) {
            Variant next = CustomizationUtils.nextAlphabetically(variant, Variant.values());
            shopkeeper.setVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new CowVariantCustomization(next);
        }
    }

}
