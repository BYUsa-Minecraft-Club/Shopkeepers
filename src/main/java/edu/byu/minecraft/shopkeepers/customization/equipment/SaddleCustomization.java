package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class SaddleCustomization {
    static <E extends MobEntity> void addSaddleCustomization(E entity, List<EquipmentCustomization<E>> list) {
        if(entity.canEquip(new ItemStack(Items.SADDLE), EquipmentSlot.SADDLE)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.SADDLE, "saddle", Items.SADDLE));
        }
    }
}
