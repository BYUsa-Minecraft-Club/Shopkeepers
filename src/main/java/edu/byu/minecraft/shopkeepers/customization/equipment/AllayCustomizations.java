package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.Items;

public class AllayCustomizations extends MobEquipmentCustomizations<AllayEntity> {
    protected AllayCustomizations() {
        super(EquipmentSlot.MAINHAND, "held item", Items.CAKE);
    }
}
