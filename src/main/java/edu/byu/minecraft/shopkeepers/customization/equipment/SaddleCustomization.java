package edu.byu.minecraft.shopkeepers.customization.equipment;

import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SaddleCustomization {
    static <E extends LivingEntity> void addSaddleCustomization(E entity, List<EquipmentCustomization<E>> list) {
        if(entity.isEquippableInSlot(new ItemStack(Items.SADDLE), EquipmentSlot.SADDLE)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.SADDLE, "saddle", Items.SADDLE));
        }
    }
}
