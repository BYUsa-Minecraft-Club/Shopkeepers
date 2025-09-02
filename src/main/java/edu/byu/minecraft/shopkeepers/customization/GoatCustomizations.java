package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class GoatCustomizations {
    public static List<ShopkeeperCustomization<GoatEntity>> getGoatCustomizations(GoatEntity entity) {
        List<ShopkeeperCustomization<GoatEntity>> customizations = new ArrayList<>();
        customizations.add(new GoatRightHornCustomization(entity.hasRightHorn()));
        customizations.add(new GoatLeftHornCustomization(entity.hasLeftHorn()));
        return customizations;
    }

    private record GoatLeftHornCustomization(boolean hasHorn)
            implements ShopkeeperCustomization<GoatEntity> {

        @Override
        public String customizationDescription() {
            return "Left Horn";
        }

        @Override
        public String currentDescription() {
            return hasHorn ? "Has Left Horn" : "No Left Horn";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasHorn ? Items.GOAT_HORN : Items.BARRIER;
        }

        @Override
        public ShopkeeperCustomization<GoatEntity> setNext(GoatEntity shopkeeper) {
            boolean hasHorn = shopkeeper.hasLeftHorn();
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(false, !hasHorn);
            return new GoatLeftHornCustomization(!hasHorn);
        }
    }

    private record GoatRightHornCustomization(boolean hasHorn)
            implements ShopkeeperCustomization<GoatEntity> {

        @Override
        public String customizationDescription() {
            return "Right Horn";
        }

        @Override
        public String currentDescription() {
            return hasHorn ? "Has Right Horn" : "No Right Horn";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasHorn ? Items.GOAT_HORN : Items.BARRIER;
        }

        @Override
        public ShopkeeperCustomization<GoatEntity> setNext(GoatEntity shopkeeper) {
            boolean hasHorn = shopkeeper.hasRightHorn();
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(true, !hasHorn);
            return new GoatRightHornCustomization(!hasHorn);
        }
    }

    public interface GoatEditor {
        void shopkeepers$setHasHorn(boolean rightHorn, boolean hasHorn);
    }

}
