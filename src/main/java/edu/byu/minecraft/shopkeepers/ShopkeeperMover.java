package edu.byu.minecraft.shopkeepers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class ShopkeeperMover {
    public static final String SHOPKEEPER_ID_KEY = "shopkeepers:shopkeeperId";

    private static final Cache<UUID, Entity> SHOPKEEPER_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.HOURS)
            .maximumSize(100)
            .build();

    public static boolean moved(ItemStack itemStack, ServerPlayer player) {
        CustomData customNbt = itemStack.getComponents().get(DataComponents.CUSTOM_DATA);
        boolean isShopkeeperTeleportItem = customNbt != null && customNbt.copyTag().contains(SHOPKEEPER_ID_KEY);

        if(isShopkeeperTeleportItem) {
            Tag nbtElement = customNbt.copyTag().get(SHOPKEEPER_ID_KEY);
            Optional<UUID> uuidResult = nbtElement.asString().map(UUID::fromString);
            if (uuidResult.isEmpty()) {
                player.sendSystemMessage(Component.nullToEmpty(
                        "Shopkeeper teleport key is present but unreadable. Please try again or contact an admin"));
            } else {
                UUID shopkeeperId = uuidResult.orElseThrow();
                ShopkeeperData data = Shopkeepers.getData().getShopkeeperData().get(shopkeeperId);
                if(data == null) {
                    player.sendSystemMessage(Component.nullToEmpty("An unknown error occurred. Please try again or contact an admin"));
                }
                else if (!data.owners().contains(player.getUUID()) && !Shopkeepers.isAdmin(player)) {
                    player.sendSystemMessage(Component.nullToEmpty("Error: You are not an admin and do not own this shop"));
                }
                else {
                    Entity shopkeeper = getShopkeeper(shopkeeperId, player.level());
                    if (shopkeeper == null) {
                        player.sendSystemMessage(
                                Component.nullToEmpty("Unable to locate shopkeeper. Please try again or contact an admin."));
                    } else {
                        if (!shopkeeper.level().isLoaded(shopkeeper.blockPosition())) {
                            player.sendSystemMessage(Component.nullToEmpty("Warning: shopkeeper is currently unloaded. " +
                                    "This may result in an unsuccessful teleport. If unsuccessful, " +
                                    "please ensure the shopkeeper is loaded before trying again"));
                        }
                        shopkeeper.teleport(
                                new TeleportTransition(player.level(), player.position(), Vec3.ZERO, player.getYRot(),
                                        player.getXRot(), TeleportTransition.DO_NOTHING));
                    }
                }
            }

            itemStack.setCount(0);
        }

        return isShopkeeperTeleportItem;
    }

    public static void addToShopkeeperCache(Entity shopkeeper) {
        SHOPKEEPER_CACHE.put(shopkeeper.getUUID(), shopkeeper);
    }

    private static Entity getShopkeeper(UUID shopkeeperId, ServerLevel world) {
        Entity shopkeeper = SHOPKEEPER_CACHE.getIfPresent(shopkeeperId);
        if (shopkeeper == null) {
            shopkeeper = world.getEntity(shopkeeperId);
        }
        return shopkeeper;
    }
}
