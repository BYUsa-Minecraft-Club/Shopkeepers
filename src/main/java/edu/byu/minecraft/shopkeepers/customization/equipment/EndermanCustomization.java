package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class EndermanCustomization implements EquipmentCustomization<EnderMan> {
    private static final Item DEFAULT_DISPLAY_ITEM = Items.GRASS_BLOCK;
    private Item displayItem;

    public EndermanCustomization(EnderMan enderman) {
        ItemStack initalStack = getInitalStack(enderman);
        displayItem = initalStack.isEmpty() ? DEFAULT_DISPLAY_ITEM : initalStack.getItem();
    }

    @Override
    public String validate(EnderMan entity, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        if (stack.getCount() > 1) return "Stack must have exactly one item";
        if (!stack.getComponentsPatch().isEmpty()) return "Stack cannot have any changed components";
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
    public ItemStack getInitalStack(EnderMan entity) {
        BlockState carriedBlock = entity.getCarriedBlock();
        return carriedBlock == null ? ItemStack.EMPTY : carriedBlock.getBlock().asItem().getDefaultInstance();
    }

    @Override
    public void updateEquipment(EnderMan enderman, ItemStack is) {
        if (is != null && !is.isEmpty() && is.getItem() instanceof BlockItem bi) {
            enderman.setCarriedBlock(bi.getBlock().defaultBlockState());
            displayItem = is.getItem();
        } else {
            enderman.setCarriedBlock(null);
            displayItem = DEFAULT_DISPLAY_ITEM;
        }
    }
}
