package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BatCustomizations {

    public static List<AppearanceCustomization<Bat>> getBatCustomizations(Bat bat) {
        List<AppearanceCustomization<Bat>> customizations = new ArrayList<>();
        customizations.add(new BatRoostingCustomization(bat.isResting()));
        return customizations;
    }

    private record BatRoostingCustomization(boolean isRoosting) implements AppearanceCustomization<Bat> {

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
            return isRoosting ? Items.DYE.lime() : Items.DYE.red();
        }

        @Override
        public AppearanceCustomization<Bat> setNext(Bat shopkeeper) {
            shopkeeper.setResting(!isRoosting);
            return new BatRoostingCustomization(!isRoosting);
        }
    }
}
