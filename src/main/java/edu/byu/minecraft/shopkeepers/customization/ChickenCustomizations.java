package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.passive.ChickenVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public class ChickenCustomizations {
    public static List<ShopkeeperCustomization<ChickenEntity>> getChickenCustomizations(ChickenEntity entity) {
        List<ShopkeeperCustomization<ChickenEntity>> customizations = new ArrayList<>();
        customizations.add(new ChickenVariantCustomization(entity));
        return customizations;
    }

    private record ChickenVariantCustomization(Variant variant)
            implements ShopkeeperCustomization<ChickenEntity> {
        private enum Variant {
            TEMPERATE(ChickenVariants.TEMPERATE),
            WARM(ChickenVariants.WARM),
            COLD(ChickenVariants.COLD);
            
            private final RegistryKey<ChickenVariant> key;
            private Variant(RegistryKey<ChickenVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<ChickenVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private ChickenVariantCustomization(ChickenEntity chicken) {
            this(Variant.of(chicken.getVariant().getKey().orElseThrow()));
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
                case TEMPERATE -> Items.WHITE_DYE;
                case WARM -> Items.ORANGE_DYE;
                case COLD -> Items.GRAY_DYE;
            };
        }

        @Override
        public ShopkeeperCustomization<ChickenEntity> setNext(ChickenEntity shopkeeper) {
            Variant next = CustomizationUtils.nextAlphabetically(variant, Variant.values());
            shopkeeper.setVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new ChickenVariantCustomization(next);
        }
    }

}
