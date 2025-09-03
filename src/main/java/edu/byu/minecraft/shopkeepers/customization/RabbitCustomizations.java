package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.RabbitEntityVariantSetter;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class RabbitCustomizations {

    public static List<ShopkeeperCustomization<RabbitEntity>> getRabbitCustomizations(RabbitEntity rabbit) {
        List<ShopkeeperCustomization<RabbitEntity>> customizations = new ArrayList<>();
        customizations.add(new RabbitVariantCustomization(rabbit.getVariant()));
        return customizations;
    }

    private record RabbitVariantCustomization(RabbitEntity.Variant variant) implements ShopkeeperCustomization<RabbitEntity> {

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
            public ShopkeeperCustomization<RabbitEntity> setNext(RabbitEntity shopkeeper) {
                RabbitEntity.Variant next = CustomizationUtils.nextAlphabetically(variant, RabbitEntity.Variant.values());
                ((RabbitEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new RabbitVariantCustomization(next);
            }
        }
}
