package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TameableMobCustomizations {

    public static <E extends TamableAnimal> List<AppearanceCustomization<E>> getTameableCustomizations(E entity) {
        List<AppearanceCustomization<E>> customizations = new ArrayList<>();
        customizations.add(new TamedCustomization<>(entity));
        customizations.add(new SittingCustomization<>(entity));
        return customizations;
    }

    static class TamedCustomization<E extends TamableAnimal> implements AppearanceCustomization<E> {

        private final boolean isTamed;

        private TamedCustomization(boolean isTamed) {
            this.isTamed = isTamed;
        }

        private TamedCustomization(TamableAnimal tameableEntity) {
            this(tameableEntity.isTame());
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
        public AppearanceCustomization<E> setNext(TamableAnimal shopkeeper) {
            shopkeeper.setTame(!isTamed, true);
            return new TamedCustomization<>(!isTamed);
        }
    }

    static class SittingCustomization<E extends TamableAnimal> implements AppearanceCustomization<E> {
        private final boolean isSitting;

        private SittingCustomization(boolean isSitting) {
            this.isSitting = isSitting;
        }

        private SittingCustomization(TamableAnimal tameableEntity) {
            this(tameableEntity.isOrderedToSit());
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
        public AppearanceCustomization<E> setNext(TamableAnimal shopkeeper) {
            shopkeeper.setOrderedToSit(!isSitting);
            shopkeeper.setInSittingPose(!isSitting);
            return new SittingCustomization<>(!isSitting);
        }
    }

}
