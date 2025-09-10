package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class BodyArmorCustomization {
    static <E extends MobEntity> void addBodyArmorCustomization(E entity, List<EquipmentCustomization<E>> list) {
        if(entity.canEquip(new ItemStack(Items.LEATHER_HORSE_ARMOR), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "armor", Items.LEATHER_HORSE_ARMOR));
        }

        if(entity.canEquip(new ItemStack(Items.WOLF_ARMOR), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "armor", Items.WOLF_ARMOR));
        }

        if(entity.canEquip(new ItemStack(Items.WHITE_HARNESS), EquipmentSlot.BODY)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.BODY, "harness", Items.WHITE_HARNESS));
        }
    }
}
