package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AllayCustomizations implements EquipmentCustomization<AllayEntity> {
    @Override
    public String validate(AllayEntity entity, ItemStack stack) {
        return (stack == null || stack.getCount() <= 1) ? null : "Stack must have exactly one item";
    }

    @Override
    public Item getDescriptionItem() {
        return Items.CAKE;
    }

    @Override
    public String equipmentSlotDescription() {
        return "held item";
    }

    @Override
    public ItemStack getInitalStack(AllayEntity entity) {
        return entity.getStackInHand(Hand.MAIN_HAND);
    }

    @Override
    public void updateEquipment(AllayEntity entity, ItemStack stack) {
        entity.setStackInHand(Hand.MAIN_HAND, stack);
    }
}
