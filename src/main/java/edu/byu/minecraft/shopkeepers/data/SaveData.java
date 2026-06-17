package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import java.util.*;

public class SaveData extends SavedData {
    public static final Codec<SaveData> OLD_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, ShopkeeperData.CODEC).fieldOf("data").forGetter(SaveData::getShopkeeperData),
            EntityType.CODEC.listOf().fieldOf("allowedShopkeepers").forGetter(SaveData::getAllowedShopkeepers),
            Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, Codec.STRING).fieldOf("players").forGetter(SaveData::getPlayers),
            Codec.INT.fieldOf("maxOwnedShops").forGetter(SaveData::getMaxOwnedShops)
    ).apply(instance, (data, allowed, players, max) ->
            new SaveData(data, allowed, players, max, Map.of())));

    public static final Codec<SaveData> NEW_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, ShopkeeperData.CODEC).fieldOf("data").forGetter(SaveData::getShopkeeperData),
            EntityType.CODEC.listOf().fieldOf("allowedShopkeepers").forGetter(SaveData::getAllowedShopkeepers),
            Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, Codec.STRING).fieldOf("players").forGetter(SaveData::getPlayers),
            Codec.INT.fieldOf("maxOwnedShops").forGetter(SaveData::getMaxOwnedShops),
            Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, Codec.INT).fieldOf("playerMaxOwnedShops").forGetter(SaveData::getPlayerMaxOwnedShops)
    ).apply(instance, SaveData::new));

    public static final Codec<SaveData> CODEC = Codec.withAlternative(NEW_CODEC, OLD_CODEC);

    private static final List<EntityType<?>> DEFAULT_ALLOWED_ENTITIES = List.of(EntityType.VILLAGER);
    private static final int DEFAULT_MAX_SHOPS = 15;

    private final Map<UUID, ShopkeeperData> shopkeeperData;

    private final List<EntityType<?>> allowedShopkeepers;

    private final Map<UUID, String> players;

    private int maxOwnedShops;

    private final Map<UUID, Integer> playerMaxOwnedShops;

    public SaveData(Map<UUID, ShopkeeperData> shopkeeperData, List<EntityType<?>> allowedShopkeepers, Map<UUID, String> players,
                    int maxOwnedShops, Map<UUID, Integer> playerMaxOwnedShops) {
        this.shopkeeperData = new HashMap<>(shopkeeperData);
        this.allowedShopkeepers = new ArrayList<>(allowedShopkeepers);
        this.players = new HashMap<>(players);
        this.maxOwnedShops = maxOwnedShops;
        this.playerMaxOwnedShops = new HashMap<>(playerMaxOwnedShops);
    }

    private SaveData() {
        this(Map.of(), DEFAULT_ALLOWED_ENTITIES, Map.of(), DEFAULT_MAX_SHOPS, Map.of());
    }

    public static SaveData getServerState(MinecraftServer server) {
        return server.overworld().getDataStorage()
                .computeIfAbsent(new SavedDataType<>(Shopkeepers.MOD_ID, SaveData::new, CODEC, null));
    }

    public void checkPlayer(ServerPlayer player) {
        if(!players.containsKey(player.getUUID()) ||
                !Objects.equals(players.get(player.getUUID()), player.getGameProfile().name())) {
            players.put(player.getUUID(), player.getGameProfile().name());
            setDirty();
        }
    }

    public Map<UUID, ShopkeeperData> getShopkeeperData() {
        return shopkeeperData;
    }

    public List<EntityType<?>> getAllowedShopkeepers() {
        return allowedShopkeepers;
    }

    public Map<UUID, String> getPlayers() {
        return players;
    }

    public int getMaxOwnedShops() {
        return maxOwnedShops;
    }

    public void setMaxOwnedShops(int maxOwnedShops) {
        this.maxOwnedShops = maxOwnedShops;
    }

    public Map<UUID, Integer> getPlayerMaxOwnedShops() {
        return playerMaxOwnedShops;
    }
}
