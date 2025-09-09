package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AllayCustomizations {
    public static HeldItemCustomization getHeldItemCustomization(AllayEntity allay) {
        HeldItemCustomization.HeldItemValidator validator = (is) ->
                (is == null || is.getCount() <= 1) ? null : "Stack must have exactly one item";

        Supplier<ItemStack> initialItem = () -> allay.getStackInHand(Hand.MAIN_HAND);

        Consumer<ItemStack> onUpdateStack = (is) -> {
            if (!ItemStack.areEqual(is, allay.getStackInHand(Hand.MAIN_HAND))) {
                allay.setStackInHand(Hand.MAIN_HAND, is);
            }
        };

        return new HeldItemCustomization(validator, initialItem, onUpdateStack);
    }
}
