package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.AxolotlEntityVariantSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AxolotlCustomizations {

    public static List<AppearanceCustomization<Axolotl>> getAxolotlCustomizations(Axolotl axolotl) {
        List<AppearanceCustomization<Axolotl>> customizations = new ArrayList<>();
        customizations.add(new AxolotlVariantCustomization(axolotl.getVariant()));
        return customizations;
    }

    private record AxolotlVariantCustomization(Axolotl.Variant variant) implements
            AppearanceCustomization<Axolotl> {

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
                    case LUCY -> Items.DYE.pink();
                    case WILD -> Items.DYE.brown();
                    case GOLD -> Items.DYE.yellow();
                    case CYAN -> Items.DYE.lightBlue();
                    case BLUE -> Items.DYE.blue();
                };
            }

            @Override
            public AppearanceCustomization<Axolotl> setNext(Axolotl shopkeeper) {
                Axolotl.Variant next = CustomizationUtils.nextEnum(variant, Axolotl.Variant.values());
                ((AxolotlEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new AxolotlVariantCustomization(next);
            }
        }
}
