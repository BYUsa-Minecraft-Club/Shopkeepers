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
                    case BROWN -> Items.BROWN_DYE;
                    case WHITE -> Items.WHITE_DYE;
                    case BLACK -> Items.BLACK_DYE;
                    case WHITE_SPLOTCHED -> Items.GRAY_DYE;
                    case GOLD -> Items.YELLOW_DYE;
                    case SALT -> Items.ORANGE_DYE;
                    case EVIL -> Items.RED_DYE;
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
