package edu.byu.minecraft.shopkeepers.customization.appearance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ArmadilloCustomizations {

    public static List<AppearanceCustomization<Armadillo>> getArmadilloCustomizations(Armadillo armadillo) {
        List<AppearanceCustomization<Armadillo>> customizations = new ArrayList<>();
        customizations.add(new ArmadilloPoseCustomization(armadillo));
        return customizations;
    }

    private record ArmadilloPoseCustomization(boolean isRolledUp)
            implements AppearanceCustomization<Armadillo> {

        private ArmadilloPoseCustomization(Armadillo armadillo) {
            this(armadillo.getState() == Armadillo.ArmadilloState.SCARED || armadillo.getState() == Armadillo.ArmadilloState.ROLLING);
        }
        @Override
        public String customizationDescription() {
            return "State";
        }

        @Override
        public String currentDescription() {
            return isRolledUp ? "Scared" : "Not Scared";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isRolledUp ? Items.SHIELD : Items.ARMADILLO_SCUTE;
        }

        @Override
        public AppearanceCustomization<Armadillo> setNext(Armadillo shopkeeper) {
            shopkeeper.switchToState(isRolledUp ? Armadillo.ArmadilloState.UNROLLING : Armadillo.ArmadilloState.ROLLING);
            return new ArmadilloPoseCustomization(!isRolledUp);
        }
    }
}
