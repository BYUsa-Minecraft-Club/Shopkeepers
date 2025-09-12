package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CamelCustomizations {

    public static List<AppearanceCustomization<CamelEntity>> getCamelCustomizations(CamelEntity camel) {
        List<AppearanceCustomization<CamelEntity>> customizations = new ArrayList<>();
        customizations.add(new CamelSittingCustomization(camel));
        return customizations;
    }

    private record CamelSittingCustomization(boolean isSitting)
            implements AppearanceCustomization<CamelEntity> {

        private CamelSittingCustomization(CamelEntity camel) {
            this(camel.isSitting());
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
        public AppearanceCustomization<CamelEntity> setNext(CamelEntity shopkeeper) {
            if(isSitting) {
                shopkeeper.startStanding();
            }
            else {
                shopkeeper.startSitting();
            }
            return new CamelSittingCustomization(!isSitting);
        }
    }
}
