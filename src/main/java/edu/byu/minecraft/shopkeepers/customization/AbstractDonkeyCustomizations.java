package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class AbstractDonkeyCustomizations {

    public static <E extends AbstractDonkeyEntity> List<ShopkeeperCustomization<E>> getAbstractDonkeyCustomizations(E entity) {
        List<ShopkeeperCustomization<E>> customizations = new ArrayList<>();
        customizations.add(new ChestedCustomization<>(entity));
        return customizations;
    }

    static class ChestedCustomization<E extends AbstractDonkeyEntity> implements ShopkeeperCustomization<E> {

        private final boolean hasChest;

        ChestedCustomization(boolean isTamed) {
            this.hasChest = isTamed;
        }

        ChestedCustomization(AbstractDonkeyEntity entity) {
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
        public ShopkeeperCustomization<E> setNext(AbstractDonkeyEntity shopkeeper) {
            shopkeeper.setHasChest(!hasChest);
            return new ChestedCustomization<>(!hasChest);
        }
    }


}
