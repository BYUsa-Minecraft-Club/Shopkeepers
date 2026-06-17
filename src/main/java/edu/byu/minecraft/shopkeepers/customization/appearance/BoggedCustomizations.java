package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BoggedCustomizations {

    public static List<AppearanceCustomization<Bogged>> getBoggedCustomizations(Bogged bogged) {
        List<AppearanceCustomization<Bogged>> customizations = new ArrayList<>();
        customizations.add(new BoggedShearedCustomization(bogged));
        return customizations;
    }

    private record BoggedShearedCustomization(boolean isSheared)
            implements AppearanceCustomization<Bogged> {

        private BoggedShearedCustomization(Bogged bogged) {
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
        public AppearanceCustomization<Bogged> setNext(Bogged shopkeeper) {
            shopkeeper.setSheared(!isSheared());
            return new BoggedShearedCustomization(!isSheared);
        }
    }
}
