package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.Items;
import java.util.List;

public class HeldItemCustomizations {
    static <E extends LivingEntity> void addHeldItemCustomizations(E entity, List<EquipmentCustomization<E>> list) {
        if(canVisuallyDualWield(entity)) {
            list.add(new HandEquipmentCustomizations<>(InteractionHand.MAIN_HAND, "main hand held item", Items.IRON_SWORD));
            list.add(new HandEquipmentCustomizations<>(InteractionHand.OFF_HAND, "off hand held item", Items.SHIELD));
        }
        else if(canVisuallyHaveOneItem(entity)) {
            list.add(new HandEquipmentCustomizations<>(InteractionHand.MAIN_HAND, "held item", Items.PAPER));
        }
    }


    private static boolean canVisuallyDualWield(LivingEntity mob) {
        return mob instanceof Zombie || mob instanceof AbstractSkeleton ||
                mob instanceof AbstractPiglin || mob instanceof Pillager ||
                mob instanceof Giant || mob instanceof Vex ||
                mob instanceof ArmorStand || mob instanceof Avatar;
    }

    private static boolean canVisuallyHaveOneItem(LivingEntity mob) {
        return mob instanceof Allay || mob instanceof Fox ||
                mob instanceof Dolphin || //remove if not desired
                mob instanceof AbstractVillager || mob instanceof Witch;
        //MerchantEntity covers villager & wandering trader
    }

}
