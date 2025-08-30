package edu.byu.minecraft.shopkeepers.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InteractionLocks {
    private final Map<UUID, UUID> shopkeeperPlayerLockMap = new HashMap<>();

    /**
     * Attempts to acquire lock for specified shopkeeper
     *
     * @param shopkeeperId UUID of shopkeeper to lock
     * @param playerId UUID of player attempting to lock shopkeeper
     * @return null if lock acquired successfully, otherwise the UUID of the player holding the lock
     */
    public UUID tryAcquireLock(UUID shopkeeperId, UUID playerId) {
        if (shopkeeperPlayerLockMap.containsKey(shopkeeperId)) {
            return shopkeeperPlayerLockMap.get(shopkeeperId);
        } else {
            shopkeeperPlayerLockMap.put(shopkeeperId, playerId);
            return null;
        }
    }

    public void releaseLock(UUID shopkeeperId, UUID playerId) {
        if(Objects.equals(shopkeeperPlayerLockMap.get(shopkeeperId), playerId)) {
            shopkeeperPlayerLockMap.remove(shopkeeperId);
        }
    }
}
