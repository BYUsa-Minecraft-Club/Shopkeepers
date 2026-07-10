package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.LlamaEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class LlamaCustomizations {

    public static List<AppearanceCustomization<Llama>> getLlamaCustomizations(Llama llama) {
        List<AppearanceCustomization<Llama>> customizations = new ArrayList<>();
        customizations.add(new LlamaVariantCustomization(llama.getVariant()));
        customizations.add(new AbstractDonkeyCustomizations.ChestedCustomization<>(llama));
        return customizations;
    }

    private record LlamaVariantCustomization(Llama.Variant variant) implements
            AppearanceCustomization<Llama> {

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
                    case CREAMY -> Items.DYE.yellow();
                    case WHITE -> Items.DYE.white();
                    case BROWN -> Items.DYE.brown();
                    case GRAY -> Items.DYE.lightGray();
                };
            }

            @Override
            public AppearanceCustomization<Llama> setNext(Llama shopkeeper) {
                Llama.Variant next = CustomizationUtils.nextEnum(variant, Llama.Variant.values());
                ((LlamaEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new LlamaVariantCustomization(next);
            }
        }

}
