package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EndermanCustomization implements EquipmentCustomization<EndermanEntity> {
    @Override
    public String validate(EndermanEntity entity, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getCount() > 1) return "Stack must have exactly one item";
        if (!stack.getComponentChanges().isEmpty()) return "Stack cannot have any changed components";
        return (stack.getItem() instanceof BlockItem) ? null : "Stack must be a block item";
    }

    @Override
    public Item getDescriptionItem() {
        return Items.GRASS_BLOCK;
    }

    @Override
    public String equipmentSlotDescription() {
        return "held block";
    }

    @Override
    public ItemStack getInitalStack(EndermanEntity entity) {
        BlockState carriedBlock = entity.getCarriedBlock();
        return carriedBlock == null ? ItemStack.EMPTY : carriedBlock.getBlock().asItem().getDefaultStack();
    }

    @Override
    public void updateEquipment(EndermanEntity enderman, ItemStack is) {
        if (is != null && !is.isEmpty() && is.getItem() instanceof BlockItem bi) {
            enderman.setCarriedBlock(bi.getBlock().getDefaultState());
        } else {
            enderman.setCarriedBlock(null);
        }
    }
}
