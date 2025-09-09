package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class GoatCustomizations {
    public static List<AppearanceCustomization<GoatEntity>> getGoatCustomizations(GoatEntity entity) {
        List<AppearanceCustomization<GoatEntity>> customizations = new ArrayList<>();
        customizations.add(new GoatRightHornCustomization(entity.hasRightHorn()));
        customizations.add(new GoatLeftHornCustomization(entity.hasLeftHorn()));
        return customizations;
    }

    private record GoatLeftHornCustomization(boolean hasHorn)
            implements AppearanceCustomization<GoatEntity> {

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
        public AppearanceCustomization<GoatEntity> setNext(GoatEntity shopkeeper) {
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(false, !hasHorn);
            return new GoatLeftHornCustomization(!hasHorn);
        }
    }

    private record GoatRightHornCustomization(boolean hasHorn)
            implements AppearanceCustomization<GoatEntity> {

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
        public AppearanceCustomization<GoatEntity> setNext(GoatEntity shopkeeper) {
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(true, !hasHorn);
            return new GoatRightHornCustomization(!hasHorn);
        }
    }

    public interface GoatEditor {
        void shopkeepers$setHasHorn(boolean rightHorn, boolean hasHorn);
    }

}
