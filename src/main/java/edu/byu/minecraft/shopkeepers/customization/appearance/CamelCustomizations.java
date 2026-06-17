package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CamelCustomizations {

    public static List<AppearanceCustomization<Camel>> getCamelCustomizations(Camel camel) {
        List<AppearanceCustomization<Camel>> customizations = new ArrayList<>();
        customizations.add(new CamelSittingCustomization(camel));
        return customizations;
    }

    private record CamelSittingCustomization(boolean isSitting)
            implements AppearanceCustomization<Camel> {

        private CamelSittingCustomization(Camel camel) {
            this(camel.isCamelSitting());
        }
        @Override
        public String customizationDescription() {
            return "Sitting";
        }

        @Override
        public String currentDescription() {
            return isSitting ? "Sitting" : "Standing";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isSitting ? Items.ACACIA_SLAB : Items.ACACIA_PLANKS;
        }

        @Override
        public AppearanceCustomization<Camel> setNext(Camel shopkeeper) {
            if(isSitting) {
                shopkeeper.standUp();
            }
            else {
                shopkeeper.sitDown();
            }
            return new CamelSittingCustomization(!isSitting);
        }
    }
}
