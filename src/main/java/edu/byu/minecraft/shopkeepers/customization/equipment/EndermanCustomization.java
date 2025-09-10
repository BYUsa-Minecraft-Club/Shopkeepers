package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EndermanCustomization implements EquipmentCustomization<EndermanEntity> {
    private static final Item DEFAULT_DISPLAY_ITEM = Items.GRASS_BLOCK;
    private Item displayItem;

    public EndermanCustomization(EndermanEntity enderman) {
        ItemStack initalStack = getInitalStack(enderman);
        displayItem = initalStack.isEmpty() ? DEFAULT_DISPLAY_ITEM : initalStack.getItem();
    }

    @Override
    public String validate(EndermanEntity entity, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getCount() > 1) return "Stack must have exactly one item";
        if (!stack.getComponentChanges().isEmpty()) return "Stack cannot have any changed components";
        return (stack.getItem() instanceof BlockItem) ? null : "Stack must be a block item";
    }

    @Override
    public Item getDescriptionItem() {
        return displayItem;
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
            displayItem = is.getItem();
        } else {
            enderman.setCarriedBlock(null);
            displayItem = DEFAULT_DISPLAY_ITEM;
        }
    }
}
