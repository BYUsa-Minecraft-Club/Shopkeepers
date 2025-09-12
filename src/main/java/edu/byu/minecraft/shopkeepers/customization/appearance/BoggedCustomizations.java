package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BoggedCustomizations {

    public static List<AppearanceCustomization<BoggedEntity>> getBoggedCustomizations(BoggedEntity bogged) {
        List<AppearanceCustomization<BoggedEntity>> customizations = new ArrayList<>();
        customizations.add(new BoggedShearedCustomization(bogged));
        return customizations;
    }

    private record BoggedShearedCustomization(boolean isSheared)
            implements AppearanceCustomization<BoggedEntity> {

        private BoggedShearedCustomization(BoggedEntity bogged) {
            this(bogged.isSheared());
        }
        @Override
        public String customizationDescription() {
            return "Sheared";
        }

        @Override
        public String currentDescription() {
            return isSheared ? "Sheared" : "Not Sheared";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isSheared ? Items.SHEARS : Items.RED_MUSHROOM;
        }

        @Override
        public AppearanceCustomization<BoggedEntity> setNext(BoggedEntity shopkeeper) {
            shopkeeper.setSheared(!isSheared());
            return new BoggedShearedCustomization(!isSheared);
        }
    }
}
