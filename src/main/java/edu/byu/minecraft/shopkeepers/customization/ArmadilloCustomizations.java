package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;

import java.util.ArrayList;
import java.util.List;

public class ArmadilloCustomizations {

    public static List<ShopkeeperCustomization<ArmadilloEntity>> getArmadilloCustomizations(ArmadilloEntity armadillo) {
        List<ShopkeeperCustomization<ArmadilloEntity>> customizations = new ArrayList<>();
        customizations.add(new ArmadilloPoseCustomization(armadillo));
        return customizations;
    }

    private record ArmadilloPoseCustomization(boolean isRolledUp)
            implements ShopkeeperCustomization<ArmadilloEntity> {

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
        public ShopkeeperCustomization<ArmadilloEntity> setNext(ArmadilloEntity shopkeeper) {
            shopkeeper.setState(isRolledUp ? ArmadilloEntity.State.UNROLLING : ArmadilloEntity.State.ROLLING);
            return new ArmadilloPoseCustomization(!isRolledUp);
        }
    }
}
