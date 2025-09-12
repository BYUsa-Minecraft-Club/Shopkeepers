package edu.byu.minecraft.shopkeepers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.FieldDecoder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShopkeeperMover {
    public static final String SHOPKEEPER_ID_KEY = "shopkeepers:shopkeeperId";

    private static final Cache<UUID, Entity> SHOPKEEPER_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.HOURS)
            .maximumSize(100)
            .build();

    public static boolean moved(ItemStack itemStack, ServerPlayerEntity player) {
        NbtComponent customNbt = itemStack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        boolean isShopkeeperTeleportItem = customNbt != null && customNbt.contains(SHOPKEEPER_ID_KEY);

        if(isShopkeeperTeleportItem) {
            DataResult<UUID> uuidResult = customNbt.get(new FieldDecoder<>(SHOPKEEPER_ID_KEY, Uuids.CODEC));
            if (!uuidResult.isSuccess()) {
                player.sendMessage(Text.of(
                        "Shopkeeper teleport key is present but unreadable. Please try again or contact an admin"));
            } else {
                UUID shopkeeperId = uuidResult.getOrThrow();
                Entity shopkeeper = getShopkeeper(shopkeeperId, player.getWorld());
                if (shopkeeper == null) {
                    player.sendMessage(Text.of("Unable to locate shopkeeper. Please try again or contact an admin."));
                } else {
                    if(!shopkeeper.getWorld().isPosLoaded(shopkeeper.getBlockPos())) {
                        player.sendMessage(Text.of("Warning: shopkeeper is currently unloaded. " +
                                "This may result in an unsuccessful teleport. If unsuccessful, " +
                                "please ensure the shopkeeper is loaded before trying again"));
                    }
                    shopkeeper.teleportTo(new TeleportTarget(player.getWorld(), player.getPos(), Vec3d.ZERO,
                            player.getYaw(), player.getPitch(), TeleportTarget.NO_OP));
                }
            }

            itemStack.setCount(0);
        }

        return isShopkeeperTeleportItem;
    }

    public static void addToShopkeeperCache(Entity shopkeeper) {
        SHOPKEEPER_CACHE.put(shopkeeper.getUuid(), shopkeeper);
    }

    private static Entity getShopkeeper(UUID shopkeeperId, ServerWorld world) {
        Entity shopkeeper = SHOPKEEPER_CACHE.getIfPresent(shopkeeperId);
        if (shopkeeper == null) {
            shopkeeper = world.getEntity(shopkeeperId);
        }
        return shopkeeper;
    }
}
