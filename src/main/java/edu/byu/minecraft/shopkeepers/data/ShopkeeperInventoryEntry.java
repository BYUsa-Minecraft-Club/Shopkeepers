package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

public final class ShopkeeperInventoryEntry {
    public static final Codec<ShopkeeperInventoryEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(ShopkeeperInventoryEntry::getStack),
            Codec.INT.fieldOf("amount").forGetter(ShopkeeperInventoryEntry::getAmount)
    ).apply(instance, ShopkeeperInventoryEntry::new));

    private final ItemStack stack;

    private int amount;

    public ShopkeeperInventoryEntry(ItemStack stack, int amount) {
        this.stack = stack;
        this.amount = amount;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
