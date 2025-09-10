package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AllayCustomizations extends HandEquipmentCustomizations<AllayEntity> {
    public AllayCustomizations(AllayEntity allay) {
        super(Hand.MAIN_HAND, "held item",
                allay.getStackInHand(Hand.MAIN_HAND).isEmpty() ? Items.CAKE : allay.getStackInHand(Hand.MAIN_HAND).getItem());
    }
}
