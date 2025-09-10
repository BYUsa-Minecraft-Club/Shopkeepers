package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface EquipmentCustomization<E extends Entity> {
        /**
         * Validates if an ItemStack can be accepted by this mob
         *
         * @param entity the entity to test
         * @param stack the ItemStack to validate
         * @return null if the item is acceptable, otherwise a description of why the item is not acceptable
         */
        String validate(E entity, ItemStack stack);
        Item getDescriptionItem();
        String equipmentSlotDescription();
        ItemStack getInitalStack(E entity);
        void updateEquipment(E entity, ItemStack stack);

}
