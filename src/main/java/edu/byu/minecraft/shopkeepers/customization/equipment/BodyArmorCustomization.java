package edu.byu.minecraft.shopkeepers.customization.equipment;

import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BodyArmorCustomization {
    static <E extends LivingEntity> void addBodyArmorCustomization(E entity, List<EquipmentCustomization<E>> list) {
        if(entity.isEquippableInSlot(new ItemStack(Items.LEATHER_HORSE_ARMOR), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "armor", Items.LEATHER_HORSE_ARMOR));
        }

        if(entity.isEquippableInSlot(new ItemStack(Items.WOLF_ARMOR), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "armor", Items.WOLF_ARMOR));
        }

        if(entity.isEquippableInSlot(new ItemStack(Items.WHITE_HARNESS), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "harness", Items.WHITE_HARNESS));
        }

        if(entity.isEquippableInSlot(new ItemStack(Items.WHITE_CARPET), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "carpet", Items.WHITE_CARPET));
        }
    }
}
