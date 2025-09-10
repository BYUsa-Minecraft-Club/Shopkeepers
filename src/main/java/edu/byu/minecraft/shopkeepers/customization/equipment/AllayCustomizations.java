package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AllayCustomizations extends HandEquipmentCustomizations<AllayEntity> {
    public AllayCustomizations() {
        super(Hand.MAIN_HAND, "held item", Items.CAKE);
    }
}
