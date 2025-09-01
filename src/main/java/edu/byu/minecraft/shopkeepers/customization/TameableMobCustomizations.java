package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class TameableMobCustomizations {

    public static <E extends TameableEntity> List<ShopkeeperCustomization<E>> getTameableCustomizations(E entity) {
        List<ShopkeeperCustomization<E>> customizations = new ArrayList<>();
        customizations.add(new TamedCustomization<>(entity));
        customizations.add(new SittingCustomization<>(entity));
        return customizations;
    }

    static class TamedCustomization<E extends TameableEntity> implements ShopkeeperCustomization<E> {

        private final boolean isTamed;

        private TamedCustomization(boolean isTamed) {
            this.isTamed = isTamed;
        }

        private TamedCustomization(TameableEntity tameableEntity) {
            this(tameableEntity.isTamed());
        }

        @Override
        public String customizationDescription() {
            return "Tamed";
        }

        @Override
        public String currentDescription() {
            return isTamed ? "Tamed" : "Not Tamed";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isTamed ? Items.LIME_DYE : Items.RED_DYE;
        }

        @Override
        public ShopkeeperCustomization<E> setNext(TameableEntity shopkeeper) {
            shopkeeper.setTamed(!isTamed, true);
            return new TamedCustomization<>(!isTamed);
        }
    }

    static class SittingCustomization<E extends TameableEntity> implements ShopkeeperCustomization<E> {
        private final boolean isSitting;

        private SittingCustomization(boolean isSitting) {
            this.isSitting = isSitting;
        }

        private SittingCustomization(TameableEntity tameableEntity) {
            this(tameableEntity.isSitting());
        }

        @Override
        public String customizationDescription() {
            return "Sitting";
        }

        @Override
        public String currentDescription() {
            return isSitting ? "Sitting" : "Not Sitting";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isSitting ? Items.LIME_DYE : Items.RED_DYE;
        }

        @Override
        public ShopkeeperCustomization<E> setNext(TameableEntity shopkeeper) {
            shopkeeper.setSitting(!isSitting);
            shopkeeper.setInSittingPose(!isSitting);
            return new SittingCustomization<>(!isSitting);
        }
    }

}
