package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.ParrotEntityVariantSetter;
import java.util.List;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ParrotCustomizations {
    public static List<AppearanceCustomization<Parrot>> getParrotCustomizations(Parrot entity) {
        List<AppearanceCustomization<Parrot>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);

        //I don't think taming makes a visual difference for parrots
        customizations.removeIf(c -> c instanceof TameableMobCustomizations.TamedCustomization);

        customizations.add(new ParrotVariantCustomization(entity.getVariant()));
        return customizations;
    }

    private record ParrotVariantCustomization(Parrot.Variant variant)
            implements AppearanceCustomization<Parrot> {

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
                case RED_BLUE -> Items.DYE.red();
                case BLUE -> Items.DYE.blue();
                case GREEN -> Items.DYE.green();
                case YELLOW_BLUE -> Items.DYE.lightBlue();
                case GRAY -> Items.DYE.gray();
            };
        }

        @Override
        public AppearanceCustomization<Parrot> setNext(Parrot shopkeeper) {
            Parrot.Variant next = CustomizationUtils.nextEnum(variant, Parrot.Variant.values());
            ((ParrotEntityVariantSetter) shopkeeper).invokeSetVariant(next);
            return new ParrotVariantCustomization(next);
        }
    }
}
