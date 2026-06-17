package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Items;
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
        return mob instanceof Zombie || mob instanceof AbstractSkeleton ||
                mob instanceof AbstractPiglin || mob instanceof Giant ||
                mob instanceof ArmorStand || mob instanceof Avatar;
    }
}
