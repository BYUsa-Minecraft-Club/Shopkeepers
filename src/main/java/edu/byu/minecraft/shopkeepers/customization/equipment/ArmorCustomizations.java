package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.item.Items;

import java.util.List;

public class ArmorCustomizations {
    static <E extends LivingEntity> void addArmorCustomizations(E entity, List<EquipmentCustomization<E>> list) {
        if(canVisuallyEquipArmor(entity)) {
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.HEAD, "helmet", Items.LEATHER_HELMET));
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.CHEST, "chestplate", Items.LEATHER_CHESTPLATE));
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.LEGS, "leggings", Items.LEATHER_LEGGINGS));
            list.add(new MobEquipmentCustomizations<>(EquipmentSlot.FEET, "boots", Items.LEATHER_BOOTS));
        }
    }

    private static boolean canVisuallyEquipArmor(LivingEntity mob) {
        return mob instanceof ZombieEntity || mob instanceof AbstractSkeletonEntity ||
                mob instanceof AbstractPiglinEntity || mob instanceof GiantEntity ||
                mob instanceof ArmorStandEntity || mob instanceof PlayerLikeEntity;
    }
}
