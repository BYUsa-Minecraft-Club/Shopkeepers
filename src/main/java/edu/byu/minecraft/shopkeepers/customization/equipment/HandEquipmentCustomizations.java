package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HandEquipmentCustomizations<E extends LivingEntity> implements EquipmentCustomization<E> {
    private final InteractionHand hand;
    private final String slotDescription;
    private final Item descriptionItem;

    public HandEquipmentCustomizations(InteractionHand hand, String slotDescription, Item descriptionItem) {
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
        return entity.getItemInHand(hand);
    }

    @Override
    public void updateEquipment(E entity, ItemStack stack) {
        entity.setItemInHand(hand, stack);
    }
}
