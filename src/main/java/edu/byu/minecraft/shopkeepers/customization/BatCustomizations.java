package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BatCustomizations {

    public static List<ShopkeeperCustomization<BatEntity>> getBatCustomizations(BatEntity bat) {
        List<ShopkeeperCustomization<BatEntity>> customizations = new ArrayList<>();
        customizations.add(new BatRoostingCustomization(bat.isRoosting()));
        return customizations;
    }

    private record BatRoostingCustomization(boolean isRoosting) implements ShopkeeperCustomization<BatEntity> {

        @Override
        public String customizationDescription() {
            return "Roosting";
        }

        @Override
        public String currentDescription() {
            return isRoosting ? "Roosting" : "Not Roosting";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isRoosting ? Items.LIME_DYE : Items.RED_DYE;
        }

        @Override
        public ShopkeeperCustomization<BatEntity> setNext(BatEntity shopkeeper) {
            boolean isRoosting = shopkeeper.isRoosting();
            shopkeeper.setRoosting(!isRoosting);
            return new BatRoostingCustomization(!isRoosting);
        }
    }
}
