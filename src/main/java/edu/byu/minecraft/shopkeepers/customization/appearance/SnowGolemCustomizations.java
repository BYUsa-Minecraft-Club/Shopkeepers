package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SnowGolemCustomizations {

    public static List<AppearanceCustomization<SnowGolemEntity>> getSnowGolemCustomizations(SnowGolemEntity snowGolem) {
        List<AppearanceCustomization<SnowGolemEntity>> customizations = new ArrayList<>();
        customizations.add(new SnowGolemPumpkinCustomization(snowGolem.hasPumpkin()));
        return customizations;
    }

    private record SnowGolemPumpkinCustomization(boolean hasPumpkin) implements
            AppearanceCustomization<SnowGolemEntity> {

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
        public AppearanceCustomization<SnowGolemEntity> setNext(SnowGolemEntity shopkeeper) {
            shopkeeper.setHasPumpkin(!hasPumpkin);
            return new SnowGolemPumpkinCustomization(!hasPumpkin);
        }
    }
}
