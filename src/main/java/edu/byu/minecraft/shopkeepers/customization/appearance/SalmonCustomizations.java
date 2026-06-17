package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.SalmonEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SalmonCustomizations {

    public static List<AppearanceCustomization<Salmon>> getSalmonCustomizations(Salmon salmon) {
        List<AppearanceCustomization<Salmon>> customizations = new ArrayList<>();
        customizations.add(new SalmonVariantCustomization(salmon.getVariant()));
        return customizations;
    }

    private record SalmonVariantCustomization(Salmon.Variant variant) implements
            AppearanceCustomization<Salmon> {

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
            public AppearanceCustomization<Salmon> setNext(Salmon shopkeeper) {
                Salmon.Variant next = CustomizationUtils.nextEnum(variant, Salmon.Variant.values());
                ((SalmonEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new SalmonVariantCustomization(next);
            }
        }
}
