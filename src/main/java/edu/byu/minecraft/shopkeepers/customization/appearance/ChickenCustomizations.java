package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ChickenCustomizations {
    public static List<AppearanceCustomization<Chicken>> getChickenCustomizations(Chicken entity) {
        List<AppearanceCustomization<Chicken>> customizations = new ArrayList<>();
        customizations.add(new ChickenVariantCustomization(entity));
        return customizations;
    }

    private record ChickenVariantCustomization(Variant variant)
            implements AppearanceCustomization<Chicken> {
        private enum Variant {
            TEMPERATE(ChickenVariants.TEMPERATE),
            WARM(ChickenVariants.WARM),
            COLD(ChickenVariants.COLD);
            
            private final ResourceKey<ChickenVariant> key;
            private Variant(ResourceKey<ChickenVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<ChickenVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private ChickenVariantCustomization(Chicken chicken) {
            this(Variant.of(chicken.getVariant().unwrapKey().orElseThrow()));
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
                case TEMPERATE -> Items.DYE.white();
                case WARM -> Items.DYE.orange();
                case COLD -> Items.DYE.gray();
            };
        }

        @Override
        public AppearanceCustomization<Chicken> setNext(Chicken shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            shopkeeper.setVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new ChickenVariantCustomization(next);
        }
    }

}
