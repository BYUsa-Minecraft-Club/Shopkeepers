package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.WolfEntityVariationAccesor;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.WolfVariant;
import net.minecraft.entity.passive.WolfVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;

import java.util.List;

public class WolfCustomizations {
    public static List<ShopkeeperCustomization<WolfEntity>> getWolfCustomizations(WolfEntity entity) {
        List<ShopkeeperCustomization<WolfEntity>> customizations =
                TameableMobCustomizations.getTameableCustomizations(entity);
        customizations.add(new WolfVariantCustomization(entity));
        customizations.add(new WolfCollarCustomization(entity.getCollarColor()));
        customizations.add(new AngerableCustomization<>(entity));
        return customizations;
    }

    private record WolfVariantCustomization(Variant variant)
            implements ShopkeeperCustomization<WolfEntity> {
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
            
            private final RegistryKey<WolfVariant> key;
            private Variant(RegistryKey<WolfVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<WolfVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private WolfVariantCustomization(WolfEntity wolf) {
            this(Variant.of(((WolfEntityVariationAccesor) wolf).invokeGetVariant().getKey().orElseThrow()));
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
        public ShopkeeperCustomization<WolfEntity> setNext(WolfEntity shopkeeper) {
            Variant current = Variant.of(((WolfEntityVariationAccesor) shopkeeper).invokeGetVariant().getKey().orElseThrow());
            Variant next = CustomizationUtils.nextAlphabetically(current, Variant.values());
            ((WolfEntityVariationAccesor) shopkeeper).invokeSetVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new WolfVariantCustomization(next);
        }
    }
    
    private record WolfCollarCustomization(DyeColor color) implements ShopkeeperCustomization<WolfEntity> {

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
        public ShopkeeperCustomization<WolfEntity> setNext(WolfEntity shopkeeper) {
            DyeColor next = CustomizationUtils.nextAlphabetically(shopkeeper.getCollarColor(), DyeColor.values());
            ((WolfEntityVariationAccesor) shopkeeper).invokeSetCollarColor(next);
            return new WolfCollarCustomization(next);
        }
    }
}
