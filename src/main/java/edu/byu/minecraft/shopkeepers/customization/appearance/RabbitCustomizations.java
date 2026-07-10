package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.RabbitEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class RabbitCustomizations {

    public static List<AppearanceCustomization<Rabbit>> getRabbitCustomizations(Rabbit rabbit) {
        List<AppearanceCustomization<Rabbit>> customizations = new ArrayList<>();
        customizations.add(new RabbitVariantCustomization(rabbit.getVariant()));
        return customizations;
    }

    private record RabbitVariantCustomization(Rabbit.Variant variant) implements
            AppearanceCustomization<Rabbit> {

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
                    case BROWN -> Items.DYE.brown();
                    case WHITE -> Items.DYE.white();
                    case BLACK -> Items.DYE.black();
                    case WHITE_SPLOTCHED -> Items.DYE.gray();
                    case GOLD -> Items.DYE.yellow();
                    case SALT -> Items.DYE.orange();
                    case EVIL -> Items.DYE.red();
                };
            }

            @Override
            public AppearanceCustomization<Rabbit> setNext(Rabbit shopkeeper) {
                Rabbit.Variant next = CustomizationUtils.nextEnum(variant, Rabbit.Variant.values());
                ((RabbitEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new RabbitVariantCustomization(next);
            }
        }
}
