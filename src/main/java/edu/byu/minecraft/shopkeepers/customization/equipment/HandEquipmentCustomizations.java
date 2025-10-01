package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HandEquipmentCustomizations<E extends LivingEntity> implements EquipmentCustomization<E> {
    private final Hand hand;
    private final String slotDescription;
    private final Item descriptionItem;

    public HandEquipmentCustomizations(Hand hand, String slotDescription, Item descriptionItem) {
        this.hand = hand;
        this.slotDescription = slotDescription;
        this.descriptionItem = descriptionItem;
    }

    @Override
    public String validate(E entity, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getCount() > 1) return "Stack must have exactly one item";
        return null;
    }

    @Override
    public Item getDescriptionItem() {
        return descriptionItem;
    }

    @Override
    public String equipmentSlotDescription() {
        return slotDescription;
    }

    @Override
    public ItemStack getInitalStack(E entity) {
        return entity.getStackInHand(hand);
    }

    @Override
    public void updateEquipment(E entity, ItemStack stack) {
        entity.setStackInHand(hand, stack);
    }
}
