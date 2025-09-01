package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class TameableMobCustomizations {

    public static List<ShopkeeperCustomization<TameableEntity>> getTameableCustomizations(TameableEntity entity) {
        List<ShopkeeperCustomization<TameableEntity>> customizations = new ArrayList<>();
        customizations.add(new TamedCustomization(entity));
        customizations.add(new SittingCustomization(entity));
        return customizations;
    }

    private static class TamedCustomization implements ShopkeeperCustomization<TameableEntity> {

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
        public ShopkeeperCustomization<TameableEntity> setNext(TameableEntity shopkeeper) {
            shopkeeper.setTamed(!isTamed, true);
            return new TamedCustomization(!isTamed);
        }
    }

    private static class SittingCustomization implements ShopkeeperCustomization<TameableEntity> {
        private final boolean isSitting;

        private SittingCustomization(boolean isSitting) {
            this.isSitting = isSitting;
        }

        private SittingCustomization(TameableEntity tameableEntity) {
            this(tameableEntity.isInSittingPose());
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
        public ShopkeeperCustomization<TameableEntity> setNext(TameableEntity shopkeeper) {
            shopkeeper.setInSittingPose(!isSitting);
            return new SittingCustomization(!isSitting);
        }
    }

}
