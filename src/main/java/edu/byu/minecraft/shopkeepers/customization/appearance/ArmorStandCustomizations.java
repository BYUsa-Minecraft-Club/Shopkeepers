package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.mixin.invoker.ArmorStandEntityCustomizationInvoker;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ArmorStandCustomizations {

    public static List<AppearanceCustomization<ArmorStandEntity>> getArmorStandCustomizations(ArmorStandEntity armorStand) {
        List<AppearanceCustomization<ArmorStandEntity>> customizations = new ArrayList<>();
        customizations.add(new ArmorStandSmallCustomization(armorStand.isSmall()));
        customizations.add(new ArmorStandShowArmsCustomization(armorStand.shouldShowArms()));
        customizations.add(new ArmorStandShowBasePlateCustomization(armorStand.shouldShowBasePlate()));
        return customizations;
    }

    private record ArmorStandShowArmsCustomization(boolean showArms)
            implements AppearanceCustomization<ArmorStandEntity> {

        @Override
        public String customizationDescription() {
            return "Show Arms";
        }

        @Override
        public String currentDescription() {
            return showArms ? "Shown" : "Hidden";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return showArms ? Items.STICK : Items.BARRIER;
        }

        @Override
        public ArmorStandShowArmsCustomization setNext(ArmorStandEntity shopkeeper) {
            shopkeeper.setShowArms(!showArms);
            return new ArmorStandShowArmsCustomization(!showArms);
        }
    }

    private record ArmorStandShowBasePlateCustomization(boolean showBasePlate)
            implements AppearanceCustomization<ArmorStandEntity> {

        @Override
        public String customizationDescription() {
            return "Show Base Plate";
        }

        @Override
        public String currentDescription() {
            return showBasePlate ? "Shown" : "Hidden";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return showBasePlate ? Items.SMOOTH_STONE_SLAB : Items.BARRIER;
        }

        @Override
        public ArmorStandShowBasePlateCustomization setNext(ArmorStandEntity shopkeeper) {
            shopkeeper.setHideBasePlate(showBasePlate);
            return new ArmorStandShowBasePlateCustomization(!showBasePlate);
        }
    }

    private record ArmorStandSmallCustomization(boolean isSmall)
            implements AppearanceCustomization<ArmorStandEntity> {

        @Override
        public String customizationDescription() {
            return "Size";
        }

        @Override
        public String currentDescription() {
            return isSmall ? "Short" : "Tall";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isSmall ? Items.SLIME_BALL : Items.SLIME_BLOCK;
        }

        @Override
        public ArmorStandSmallCustomization setNext(ArmorStandEntity shopkeeper) {
            ((ArmorStandEntityCustomizationInvoker) shopkeeper).invokeSetSmall(!isSmall);
            return new ArmorStandSmallCustomization(!isSmall);
        }
    }
}
