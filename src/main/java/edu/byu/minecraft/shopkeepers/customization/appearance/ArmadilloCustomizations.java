package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ArmadilloCustomizations {

    public static List<AppearanceCustomization<ArmadilloEntity>> getArmadilloCustomizations(ArmadilloEntity armadillo) {
        List<AppearanceCustomization<ArmadilloEntity>> customizations = new ArrayList<>();
        customizations.add(new ArmadilloPoseCustomization(armadillo));
        return customizations;
    }

    private record ArmadilloPoseCustomization(boolean isRolledUp)
            implements AppearanceCustomization<ArmadilloEntity> {

        private ArmadilloPoseCustomization(ArmadilloEntity armadillo) {
            this(armadillo.getState() == ArmadilloEntity.State.SCARED || armadillo.getState() == ArmadilloEntity.State.ROLLING);
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
        public AppearanceCustomization<ArmadilloEntity> setNext(ArmadilloEntity shopkeeper) {
            shopkeeper.setState(isRolledUp ? ArmadilloEntity.State.UNROLLING : ArmadilloEntity.State.ROLLING);
            return new ArmadilloPoseCustomization(!isRolledUp);
        }
    }
}
