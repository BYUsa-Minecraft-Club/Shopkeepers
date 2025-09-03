package edu.byu.minecraft.shopkeepers.customization;


import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CreeperCustomizations {
    public static List<ShopkeeperCustomization<CreeperEntity>> getCreeperCustomizations(CreeperEntity entity) {
        List<ShopkeeperCustomization<CreeperEntity>> customizations = new ArrayList<>();
        customizations.add(new CreeperChargedCustomization(entity.isCharged()));
        return customizations;
    }

    private record CreeperChargedCustomization(boolean isCharged)
            implements ShopkeeperCustomization<CreeperEntity> {

        @Override
        public String customizationDescription() {
            return "Charged";
        }

        @Override
        public String currentDescription() {
            return isCharged ? "Charged" : "Not Charged";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isCharged ? Items.LIGHTNING_ROD : Items.CREEPER_HEAD;
        }

        @Override
        public ShopkeeperCustomization<CreeperEntity> setNext(CreeperEntity shopkeeper) {
            ((CreeperEditor) shopkeeper).shopkeepers$setCharged(!isCharged);
            return new CreeperChargedCustomization(!isCharged);
        }
    }

    public interface CreeperEditor {
        void shopkeepers$setCharged(boolean charged);
    }

}
