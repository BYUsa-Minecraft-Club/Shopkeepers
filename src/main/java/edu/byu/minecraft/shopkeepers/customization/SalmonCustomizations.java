package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.SalmonEntityVariantSetter;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SalmonCustomizations {

    public static List<ShopkeeperCustomization<SalmonEntity>> getSalmonCustomizations(SalmonEntity salmon) {
        List<ShopkeeperCustomization<SalmonEntity>> customizations = new ArrayList<>();
        customizations.add(new SalmonVariantCustomization(salmon.getVariant()));
        return customizations;
    }

    private record SalmonVariantCustomization(SalmonEntity.Variant variant) implements ShopkeeperCustomization<SalmonEntity> {

        @Override
            public String customizationDescription() {
                return "Size";
            }

            @Override
            public String currentDescription() {
                return CustomizationUtils.capitalize(variant.name());
            }

            @Override
            public Item getCurrentRepresentationItem() {
                return switch (variant) {
                    case SMALL -> Items.SALMON;
                    case MEDIUM -> Items.COOKED_SALMON;
                    case LARGE -> Items.SALMON_BUCKET;
                };
            }

            @Override
            public ShopkeeperCustomization<SalmonEntity> setNext(SalmonEntity shopkeeper) {
                SalmonEntity.Variant next = CustomizationUtils.nextAlphabetically(shopkeeper.getVariant(), SalmonEntity.Variant.values());
                ((SalmonEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new SalmonVariantCustomization(next);
            }
        }
}
