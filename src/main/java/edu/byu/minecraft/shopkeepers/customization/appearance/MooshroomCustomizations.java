package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.MooshroomEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MooshroomCustomizations {

    public static List<AppearanceCustomization<MushroomCow>> getMooshroomCustomizations(MushroomCow mooshroom) {
        List<AppearanceCustomization<MushroomCow>> customizations = new ArrayList<>();
        customizations.add(new MooshroomVariantCustomization(mooshroom.getVariant()));
        return customizations;
    }

    private record MooshroomVariantCustomization(MushroomCow.Variant variant) implements
            AppearanceCustomization<MushroomCow> {

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
            public AppearanceCustomization<MushroomCow> setNext(MushroomCow shopkeeper) {
                MushroomCow.Variant next = CustomizationUtils.nextEnum(variant, MushroomCow.Variant.values());
                ((MooshroomEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new MooshroomVariantCustomization(next);
            }
        }
}
