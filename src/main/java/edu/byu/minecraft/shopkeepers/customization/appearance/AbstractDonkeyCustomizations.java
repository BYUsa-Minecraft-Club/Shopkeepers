package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AbstractDonkeyCustomizations {

    public static <E extends AbstractChestedHorse> List<AppearanceCustomization<E>> getAbstractDonkeyCustomizations(E entity) {
        List<AppearanceCustomization<E>> customizations = new ArrayList<>();
        customizations.add(new ChestedCustomization<>(entity));
        return customizations;
    }

    static class ChestedCustomization<E extends AbstractChestedHorse> implements AppearanceCustomization<E> {

        private final boolean hasChest;

        ChestedCustomization(boolean isTamed) {
            this.hasChest = isTamed;
        }

        ChestedCustomization(AbstractChestedHorse entity) {
            this(entity.hasChest());
        }

        @Override
        public String customizationDescription() {
            return "Has Chest";
        }

        @Override
        public String currentDescription() {
            return hasChest ? "Has Chest" : "No Chest";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasChest ? Items.CHEST : Items.BARRIER;
        }

        @Override
        public AppearanceCustomization<E> setNext(AbstractChestedHorse shopkeeper) {
            shopkeeper.setChest(!hasChest);
            return new ChestedCustomization<>(!hasChest);
        }
    }


}
