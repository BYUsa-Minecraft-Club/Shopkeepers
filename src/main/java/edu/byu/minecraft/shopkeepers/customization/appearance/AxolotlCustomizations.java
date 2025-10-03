package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.AxolotlEntityVariantSetter;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class AxolotlCustomizations {

    public static List<AppearanceCustomization<AxolotlEntity>> getAxolotlCustomizations(AxolotlEntity axolotl) {
        List<AppearanceCustomization<AxolotlEntity>> customizations = new ArrayList<>();
        customizations.add(new AxolotlVariantCustomization(axolotl.getVariant()));
        return customizations;
    }

    private record AxolotlVariantCustomization(AxolotlEntity.Variant variant) implements
            AppearanceCustomization<AxolotlEntity> {

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
                    case LUCY -> Items.PINK_DYE;
                    case WILD -> Items.BROWN_DYE;
                    case GOLD -> Items.YELLOW_DYE;
                    case CYAN -> Items.LIGHT_BLUE_DYE;
                    case BLUE -> Items.BLUE_DYE;
                };
            }

            @Override
            public AppearanceCustomization<AxolotlEntity> setNext(AxolotlEntity shopkeeper) {
                AxolotlEntity.Variant next = CustomizationUtils.nextEnum(variant, AxolotlEntity.Variant.values());
                ((AxolotlEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new AxolotlVariantCustomization(next);
            }
        }
}
