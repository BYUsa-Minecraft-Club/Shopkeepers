package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record HeldItemCustomization(HeldItemValidator validator,
                                    Supplier<ItemStack> initalStack, Consumer<ItemStack> onUpdateStack) {
    public interface HeldItemValidator {
        /**
         * Validates if an ItemStack can be accepted by this mob
         *
         * @param stack the ItemStack to validate
         * @return null if the item is acceptable, otherwise a description of why the item is not acceptable
         */
        String validate(ItemStack stack);
    }
}
