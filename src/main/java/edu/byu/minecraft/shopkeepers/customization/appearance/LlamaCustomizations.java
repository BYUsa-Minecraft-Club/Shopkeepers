package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.LlamaEntityVariantSetter;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class LlamaCustomizations {

    public static List<AppearanceCustomization<LlamaEntity>> getLlamaCustomizations(LlamaEntity llama) {
        List<AppearanceCustomization<LlamaEntity>> customizations = new ArrayList<>();
        customizations.add(new LlamaVariantCustomization(llama.getVariant()));
        customizations.add(new AbstractDonkeyCustomizations.ChestedCustomization<>(llama));
        return customizations;
    }

    private record LlamaVariantCustomization(LlamaEntity.Variant variant) implements
            AppearanceCustomization<LlamaEntity> {

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
                    case CREAMY -> Items.YELLOW_DYE;
                    case WHITE -> Items.WHITE_DYE;
                    case BROWN -> Items.BROWN_DYE;
                    case GRAY -> Items.LIGHT_GRAY_DYE;
                };
            }

            @Override
            public AppearanceCustomization<LlamaEntity> setNext(LlamaEntity shopkeeper) {
                LlamaEntity.Variant next = CustomizationUtils.nextAlphabetically(variant, LlamaEntity.Variant.values());
                ((LlamaEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new LlamaVariantCustomization(next);
            }
        }

}
