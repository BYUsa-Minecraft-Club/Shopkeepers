package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.WolfEntityVariationAccesor;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WolfCustomizations {
    public static List<AppearanceCustomization<Wolf>> getWolfCustomizations(Wolf entity) {
        List<AppearanceCustomization<Wolf>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);
        customizations.add(new WolfVariantCustomization(entity));
        customizations.add(new WolfCollarCustomization(entity.getCollarColor()));
        customizations.add(new AngerableCustomization<>(entity));
        return customizations;
    }

    private record WolfVariantCustomization(Variant variant)
            implements AppearanceCustomization<Wolf> {
        private enum Variant {
            PALE(WolfVariants.PALE),
            SPOTTED(WolfVariants.SPOTTED),
            SNOWY(WolfVariants.SNOWY),
            BLACK(WolfVariants.BLACK),
            ASHEN(WolfVariants.ASHEN),
            RUSTY(WolfVariants.RUSTY),
            WOODS(WolfVariants.WOODS),
            CHESTNUT(WolfVariants.CHESTNUT),
            STRIPED(WolfVariants.STRIPED);
            
            private final ResourceKey<WolfVariant> key;
            private Variant(ResourceKey<WolfVariant> key) {
                this.key = key;
            }

            private static Variant of(ResourceKey<WolfVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private WolfVariantCustomization(Wolf wolf) {
            this(Variant.of(((WolfEntityVariationAccesor) wolf).invokeGetVariant().unwrapKey().orElseThrow()));
        }

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
                case PALE -> Items.LIGHT_GRAY_DYE;
                case SPOTTED -> Items.ORANGE_DYE;
                case SNOWY -> Items.WHITE_DYE;
                case BLACK -> Items.BLACK_DYE;
                case ASHEN -> Items.LIGHT_BLUE_DYE;
                case RUSTY -> Items.RED_DYE;
                case WOODS -> Items.BROWN_DYE;
                case CHESTNUT -> Items.GRAY_DYE;
                case STRIPED -> Items.YELLOW_DYE;
            };
        }

        @Override
        public AppearanceCustomization<Wolf> setNext(Wolf shopkeeper) {
            Variant next = CustomizationUtils.nextEnum(variant, Variant.values());
            ((WolfEntityVariationAccesor) shopkeeper).invokeSetVariant(shopkeeper.registryAccess().getOrThrow(next.key));
            return new WolfVariantCustomization(next);
        }
    }
    
    private record WolfCollarCustomization(DyeColor color) implements AppearanceCustomization<Wolf> {

        @Override
        public String customizationDescription() {
            return "Collar Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(color.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return CustomizationUtils.getDyeItem(color);
        }

        @Override
        public AppearanceCustomization<Wolf> setNext(Wolf shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            ((WolfEntityVariationAccesor) shopkeeper).invokeSetCollarColor(next);
            return new WolfCollarCustomization(next);
        }
    }
}
