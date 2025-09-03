package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SnowGolemCustomizations {

    public static List<ShopkeeperCustomization<SnowGolemEntity>> getSnowGolemCustomizations(SnowGolemEntity snowGolem) {
        List<ShopkeeperCustomization<SnowGolemEntity>> customizations = new ArrayList<>();
        customizations.add(new SnowGolemPumpkinCustomization(snowGolem.hasPumpkin()));
        return customizations;
    }

    private record SnowGolemPumpkinCustomization(boolean hasPumpkin) implements ShopkeeperCustomization<SnowGolemEntity> {

        @Override
        public String customizationDescription() {
            return "Pumpkin";
        }

        @Override
        public String currentDescription() {
            return hasPumpkin ? "Has Pumpkin" : "No Pumpkin";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasPumpkin ? Items.CARVED_PUMPKIN : Items.SHEARS;
        }

        @Override
        public ShopkeeperCustomization<SnowGolemEntity> setNext(SnowGolemEntity shopkeeper) {
            shopkeeper.setHasPumpkin(!hasPumpkin);
            return new SnowGolemPumpkinCustomization(!hasPumpkin);
        }
    }
}
