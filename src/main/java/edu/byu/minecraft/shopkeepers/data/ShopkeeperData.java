package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Uuids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ShopkeeperData (List<TradeData> trades, List<ShopkeeperInventoryEntry> inventory, boolean isAdmin, List<UUID> owners) {

    public static final Codec<ShopkeeperData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TradeData.CODEC.listOf().fieldOf("trades").forGetter(ShopkeeperData::trades),
            ShopkeeperInventoryEntry.CODEC.listOf().fieldOf("inventory").forGetter(ShopkeeperData::inventory),
            Codec.BOOL.fieldOf("isAdmin").forGetter(ShopkeeperData::isAdmin),
            Uuids.CODEC.listOf().fieldOf("owners").forGetter(ShopkeeperData::owners)
    ).apply(instance, (trades, inventory, isAdmin, owners) ->
            new ShopkeeperData(new ArrayList<>(trades), new ArrayList<>(inventory), isAdmin, new ArrayList<>(owners))));


    public int inventoryIndexOf(ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (ItemStack.areItemsAndComponentsEqual(inventory.get(i).getStack(), stack)) {
                return i;
            }
        }
        return -1;
    }

    public Integer inventoryGet(ItemStack stack) {
        int index = inventoryIndexOf(stack);
        if (index == -1) return null;
        return inventory.get(index).getAmount();
    }

    public void inventoryPut(ItemStack stack) {
        if(stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("stack is null or empty");
        }
        int index = inventoryIndexOf(stack);
        if (index == -1) {
            inventory.add(new ShopkeeperInventoryEntry(stack.copy(), stack.getCount()));
        }
        else {
            inventory.get(index).setAmount(stack.getCount());
        }
    }

    public void addItems(ItemStack stack, Integer numItems) {
        stack = stack.copy();
        Integer oldCount = inventoryGet(stack);
        if (oldCount == null) {
            oldCount = 0;
        }
        stack.setCount(oldCount + numItems);
        inventoryPut(stack);
    }

    public void removeItems(ItemStack stack, Integer numItems) {
        stack = stack.copy();
        Integer oldCount = inventoryGet(stack);
        if(oldCount == null) {
            throw new IllegalArgumentException("could not find item matching " + stack);
        }
        int newCount = oldCount - numItems;
        if(newCount > 0) {
            stack.setCount(newCount);
            inventoryPut(stack);
        } else {
            inventory.remove(inventoryIndexOf(stack));
        }
    }

}
