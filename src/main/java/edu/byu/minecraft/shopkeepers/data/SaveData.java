package edu.byu.minecraft.shopkeepers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.*;

public class SaveData extends PersistentState {
    public static final Codec<SaveData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.unboundedMap(Uuids.CODEC, ShopkeeperData.CODEC).fieldOf("data").forGetter(SaveData::getData),
            EntityType.CODEC.listOf().fieldOf("allowedShopkeepers").forGetter(SaveData::getAllowedShopkeepers),
            Codec.unboundedMap(Uuids.CODEC, Codec.STRING).fieldOf("players").forGetter(SaveData::getPlayers),
            Codec.INT.fieldOf("maxOwnedShops").forGetter(SaveData::getMaxOwnedShops)
    ).apply(instance, SaveData::new));

    private static final List<EntityType<?>> DEFAULT_ALLOWED_ENTITIES = List.of(EntityType.VILLAGER);
    private static final int DEFAULT_MAX_SHOPS = 5;

    private final Map<UUID, ShopkeeperData> data;

    private final List<EntityType<?>> allowedShopkeepers;

    private final Map<UUID, String> players;

    private int maxOwnedShops;

    public SaveData(Map<UUID, ShopkeeperData> data, List<EntityType<?>> allowedShopkeepers, Map<UUID, String> players,
                    int maxOwnedShops) {
        this.data = new HashMap<>(data);
        this.allowedShopkeepers = new ArrayList<>(allowedShopkeepers);
        this.players = new HashMap<>(players);
        this.maxOwnedShops = maxOwnedShops;
    }

    private SaveData() {
        this(Map.of(), DEFAULT_ALLOWED_ENTITIES, Map.of(), DEFAULT_MAX_SHOPS);
    }

    public static SaveData getServerState(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
                .getOrCreate(new PersistentStateType<>(Shopkeepers.MOD_ID, SaveData::new, CODEC, null));
    }

    public void checkPlayer(ServerPlayerEntity player) {
        if(!players.containsKey(player.getUuid()) ||
                !Objects.equals(players.get(player.getUuid()), player.getGameProfile().getName())) {
            players.put(player.getUuid(), player.getGameProfile().getName());
            markDirty();
        }
    }

    public Map<UUID, ShopkeeperData> getData() {
        return data;
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
}
