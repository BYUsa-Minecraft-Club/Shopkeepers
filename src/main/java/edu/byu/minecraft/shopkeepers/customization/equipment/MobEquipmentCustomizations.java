package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MobEquipmentCustomizations<E extends LivingEntity> implements EquipmentCustomization<E> {
    private final EquipmentSlot slot;
    private final String slotDescription;
    private final Item descriptionItem;

    public MobEquipmentCustomizations(EquipmentSlot slot, String slotDescription, Item descriptionItem) {
        this.slot = slot;
        this.slotDescription = slotDescription;
        this.descriptionItem = descriptionItem;
    }

    @Override
    public String validate(E entity, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getCount() > 1) return "Stack must have exactly one item";
        return entity.isEquippableInSlot(stack, slot) ? null :
                String.format("%s cannot be equipped in shopkeeper %s's %s slot", stack, entity.getDisplayName().getString(),
                        slot.name());
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
        return entity.getItemBySlot(slot);
    }

    @Override
    public void updateEquipment(E entity, ItemStack stack) {
        entity.setItemSlot(slot, stack);
    }
}
