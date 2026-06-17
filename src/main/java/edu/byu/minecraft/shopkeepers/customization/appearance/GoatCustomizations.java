package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GoatCustomizations {
    public static List<AppearanceCustomization<Goat>> getGoatCustomizations(Goat entity) {
        List<AppearanceCustomization<Goat>> customizations = new ArrayList<>();
        customizations.add(new GoatRightHornCustomization(entity.hasRightHorn()));
        customizations.add(new GoatLeftHornCustomization(entity.hasLeftHorn()));
        return customizations;
    }

    private record GoatLeftHornCustomization(boolean hasHorn)
            implements AppearanceCustomization<Goat> {

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
        public AppearanceCustomization<Goat> setNext(Goat shopkeeper) {
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(false, !hasHorn);
            return new GoatLeftHornCustomization(!hasHorn);
        }
    }

    private record GoatRightHornCustomization(boolean hasHorn)
            implements AppearanceCustomization<Goat> {

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
        public AppearanceCustomization<Goat> setNext(Goat shopkeeper) {
            ((GoatEditor) shopkeeper).shopkeepers$setHasHorn(true, !hasHorn);
            return new GoatRightHornCustomization(!hasHorn);
        }
    }

    public interface GoatEditor {
        void shopkeepers$setHasHorn(boolean rightHorn, boolean hasHorn);
    }

}
