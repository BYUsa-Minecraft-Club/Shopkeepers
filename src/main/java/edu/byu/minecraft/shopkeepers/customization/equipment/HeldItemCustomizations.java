package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.List;

public class HeldItemCustomizations {
    static <E extends MobEntity> void addHeldItemCustomizations(E entity, List<EquipmentCustomization<E>> list) {
        if(canVisuallyDualWield(entity)) {
            list.add(new HandEquipmentCustomizations<>(Hand.MAIN_HAND, "main hand held item", Items.IRON_SWORD));
            list.add(new HandEquipmentCustomizations<>(Hand.OFF_HAND, "off hand held item", Items.SHIELD));
        }
        else if(canVisuallyHaveOneItem(entity)) {
            list.add(new HandEquipmentCustomizations<>(Hand.MAIN_HAND, "held item", Items.PAPER));
        }
    }


    private static boolean canVisuallyDualWield(MobEntity mob) {
        return mob instanceof ZombieEntity || mob instanceof AbstractSkeletonEntity ||
                mob instanceof AbstractPiglinEntity || mob instanceof PillagerEntity ||
                mob instanceof GiantEntity || mob instanceof VexEntity;
    }

    private static boolean canVisuallyHaveOneItem(MobEntity mob) {
        return mob instanceof AllayEntity || mob instanceof FoxEntity ||
                mob instanceof DolphinEntity || //remove if not desired
                mob instanceof MerchantEntity || mob instanceof WitchEntity;
        //MerchantEntity covers villager & wandering trader
    }

}
