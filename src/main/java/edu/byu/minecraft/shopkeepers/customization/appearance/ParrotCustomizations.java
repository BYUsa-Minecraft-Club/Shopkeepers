package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.ParrotEntityVariantSetter;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

public class ParrotCustomizations {
    public static List<AppearanceCustomization<ParrotEntity>> getParrotCustomizations(ParrotEntity entity) {
        List<AppearanceCustomization<ParrotEntity>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);

        //I don't think taming makes a visual difference for parrots
        customizations.removeIf(c -> c instanceof TameableMobCustomizations.TamedCustomization);

        customizations.add(new ParrotVariantCustomization(entity.getVariant()));
        return customizations;
    }

    private record ParrotVariantCustomization(ParrotEntity.Variant variant)
            implements AppearanceCustomization<ParrotEntity> {

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
                case RED_BLUE -> Items.RED_DYE;
                case BLUE -> Items.BLUE_DYE;
                case GREEN -> Items.LIME_DYE;
                case YELLOW_BLUE -> Items.LIGHT_BLUE_DYE;
                case GRAY -> Items.GRAY_DYE;
            };
        }

        @Override
        public AppearanceCustomization<ParrotEntity> setNext(ParrotEntity shopkeeper) {
            ParrotEntity.Variant next = CustomizationUtils.nextEnum(variant, ParrotEntity.Variant.values());
            ((ParrotEntityVariantSetter) shopkeeper).invokeSetVariant(next);
            return new ParrotVariantCustomization(next);
        }
    }
}
