package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.mixin.invoker.ArmorStandEntityCustomizationInvoker;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ArmorStandCustomizations {

    public static List<AppearanceCustomization<ArmorStand>> getArmorStandCustomizations(ArmorStand armorStand) {
        List<AppearanceCustomization<ArmorStand>> customizations = new ArrayList<>();
        customizations.add(new ArmorStandSmallCustomization(armorStand.isSmall()));
        customizations.add(new ArmorStandShowArmsCustomization(armorStand.showArms()));
        customizations.add(new ArmorStandShowBasePlateCustomization(armorStand.showBasePlate()));
        return customizations;
    }

    private record ArmorStandShowArmsCustomization(boolean showArms)
            implements AppearanceCustomization<ArmorStand> {

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
        public ArmorStandShowArmsCustomization setNext(ArmorStand shopkeeper) {
            shopkeeper.setShowArms(!showArms);
            return new ArmorStandShowArmsCustomization(!showArms);
        }
    }

    private record ArmorStandShowBasePlateCustomization(boolean showBasePlate)
            implements AppearanceCustomization<ArmorStand> {

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
        public ArmorStandShowBasePlateCustomization setNext(ArmorStand shopkeeper) {
            shopkeeper.setNoBasePlate(showBasePlate);
            return new ArmorStandShowBasePlateCustomization(!showBasePlate);
        }
    }

    private record ArmorStandSmallCustomization(boolean isSmall)
            implements AppearanceCustomization<ArmorStand> {

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
        public ArmorStandSmallCustomization setNext(ArmorStand shopkeeper) {
            ((ArmorStandEntityCustomizationInvoker) shopkeeper).invokeSetSmall(!isSmall);
            return new ArmorStandSmallCustomization(!isSmall);
        }
    }
}
