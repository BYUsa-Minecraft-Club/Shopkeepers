package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record TradeData(ItemStack firstBuyItem, Optional<ItemStack> secondBuyItem, ItemStack sellItem) {
    public static final Codec<TradeData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemStack.CODEC.fieldOf("firstBuyItem").forGetter(TradeData::firstBuyItem),
        ItemStack.CODEC.optionalFieldOf("secondBuyItem").forGetter(TradeData::secondBuyItem),
        ItemStack.CODEC.fieldOf("sellItem").forGetter(TradeData::sellItem)
    ).apply(instance, TradeData::new));

    public boolean equals(Object o) {
        if(!(o instanceof TradeData other)) return false;
        if(o == this) return true;
        return ItemStack.areEqual(firstBuyItem(), other.firstBuyItem()) &&
                ((secondBuyItem().isEmpty() && other.secondBuyItem().isEmpty()) ||
                        (secondBuyItem().isPresent() && other.secondBuyItem().isPresent() &&
                                ItemStack.areEqual(secondBuyItem().get(), other.secondBuyItem().get()))) &&
                ItemStack.areEqual(sellItem(), other.sellItem());
    }
}
