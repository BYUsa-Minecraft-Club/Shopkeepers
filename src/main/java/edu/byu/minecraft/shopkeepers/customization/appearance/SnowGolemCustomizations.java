package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SnowGolemCustomizations {

    public static List<AppearanceCustomization<SnowGolem>> getSnowGolemCustomizations(SnowGolem snowGolem) {
        List<AppearanceCustomization<SnowGolem>> customizations = new ArrayList<>();
        customizations.add(new SnowGolemPumpkinCustomization(snowGolem.hasPumpkin()));
        return customizations;
    }

    private record SnowGolemPumpkinCustomization(boolean hasPumpkin) implements
            AppearanceCustomization<SnowGolem> {

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
        public AppearanceCustomization<SnowGolem> setNext(SnowGolem shopkeeper) {
            shopkeeper.setPumpkin(!hasPumpkin);
            return new SnowGolemPumpkinCustomization(!hasPumpkin);
        }
    }
}
