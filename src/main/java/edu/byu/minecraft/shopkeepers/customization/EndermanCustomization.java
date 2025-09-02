package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EndermanCustomization {
    static HeldItemCustomization getHeldItemCustomization(EndermanEntity enderman) {
        HeldItemCustomization.HeldItemValidator validator = (is) -> {
            if (is == null || is.isEmpty()) return null;
            if (is.getCount() > 1) return "Stack must have exactly one item";
            if (!is.getComponentChanges().isEmpty()) return "Stack cannot have any changed components";
            return (is.getItem() instanceof BlockItem) ? null : "Stack must be a block item";
        };

        Supplier<ItemStack> initialItem = () -> {
            BlockState carriedBlock = enderman.getCarriedBlock();
            return carriedBlock == null ? ItemStack.EMPTY : carriedBlock.getBlock().asItem().getDefaultStack();
        };

        Consumer<ItemStack> onUpdateStack = (is) -> {
            if (is != null && !is.isEmpty() && is.getItem() instanceof BlockItem bi) {
                enderman.setCarriedBlock(bi.getBlock().getDefaultState());
            } else {
                enderman.setCarriedBlock(null);
            }
        };

        return new HeldItemCustomization(validator, initialItem, onUpdateStack);
    }
}
