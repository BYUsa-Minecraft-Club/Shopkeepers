package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

public record TradeData(ItemStack firstBuyItem, Optional<ItemStack> secondBuyItem, ItemStack sellItem,
                        Map<UUID, Integer> uses) {
    public static final Codec<TradeData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemStack.CODEC.fieldOf("firstBuyItem").forGetter(TradeData::firstBuyItem),
        ItemStack.CODEC.optionalFieldOf("secondBuyItem").forGetter(TradeData::secondBuyItem),
        ItemStack.CODEC.fieldOf("sellItem").forGetter(TradeData::sellItem),
        Codec.unboundedMap(Uuids.CODEC, Codec.INT).fieldOf("uses").forGetter(TradeData::uses)
    ).apply(instance, TradeData::newMutableTradeData));

    private static TradeData newMutableTradeData(ItemStack firstBuyItem, Optional<ItemStack> secondBuyItem,
                                                 ItemStack sellItem, Map<UUID, Integer> uses) {
        return new TradeData(firstBuyItem, secondBuyItem, sellItem, new HashMap<>(uses));
    }

    public TradeData withUses(Map<UUID, Integer> uses) {
        return new TradeData(firstBuyItem, secondBuyItem, sellItem, uses);
    }

    public boolean equalsIgnoreUses(TradeData other) {
        return areEqualIgnoreUses(other, ItemStack::areEqual);
    }

    public boolean equalsIgnoreUsesAndCounts(TradeData other) {
        return areEqualIgnoreUses(other, ItemStack::areItemsAndComponentsEqual);
    }

    private boolean areEqualIgnoreUses(TradeData other, BiFunction<ItemStack, ItemStack, Boolean> compare) {
        return compare.apply(firstBuyItem(), other.firstBuyItem()) &&
                ((secondBuyItem().isEmpty() && other.secondBuyItem().isEmpty()) ||
                        (secondBuyItem().isPresent() && other.secondBuyItem().isPresent() &&
                                compare.apply(secondBuyItem().get(), other.secondBuyItem().get()))) &&
                compare.apply(sellItem(), other.sellItem());
    }
}
