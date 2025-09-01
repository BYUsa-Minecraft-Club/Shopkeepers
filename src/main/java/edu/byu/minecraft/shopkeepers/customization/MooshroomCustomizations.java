package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.MooshroomEntityVariantSetter;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class MooshroomCustomizations {

    public static List<ShopkeeperCustomization<MooshroomEntity>> getMooshroomCustomizations(MooshroomEntity mooshroom) {
        List<ShopkeeperCustomization<MooshroomEntity>> customizations = new ArrayList<>();
        customizations.add(new MooshroomVariantCustomization(mooshroom.getVariant()));
        return customizations;
    }

    private record MooshroomVariantCustomization(MooshroomEntity.Variant variant) implements ShopkeeperCustomization<MooshroomEntity> {

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
                    case RED -> Items.RED_MUSHROOM;
                    case BROWN -> Items.BROWN_MUSHROOM;
                };
            }

            @Override
            public ShopkeeperCustomization<MooshroomEntity> setNext(MooshroomEntity shopkeeper) {
                MooshroomEntity.Variant next = CustomizationUtils.nextAlphabetically(shopkeeper.getVariant());
                ((MooshroomEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new MooshroomVariantCustomization(next);
            }
        }
}
