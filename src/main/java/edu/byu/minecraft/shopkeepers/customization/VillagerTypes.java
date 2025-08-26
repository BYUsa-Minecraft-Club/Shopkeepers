package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.VillagerType;

public enum VillagerTypes {
    DESERT(VillagerType.DESERT),
    JUNGLE(VillagerType.JUNGLE),
    PLAINS(VillagerType.PLAINS),
    SAVANNA(VillagerType.SAVANNA),
    SNOW(VillagerType.SNOW),
    SWAMP(VillagerType.SWAMP),
    TAIGA(VillagerType.TAIGA);

    private final RegistryKey<VillagerType> type;
    private VillagerTypes(RegistryKey<VillagerType> type) {
        this.type = type;
    }

    public static VillagerTypes getVillagerType(RegistryKey<VillagerType> type) {
        for(VillagerTypes vt : VillagerTypes.values()) {
            if(vt.type == type) {
                return vt;
            }
        }
        throw new IllegalArgumentException("No VillagerTypes found for " + type);
    }

    public RegistryKey<VillagerType> getType() {
        return type;
    }

    public VillagerTypes getNextInCycle() {
        return switch (this) {
            case DESERT -> JUNGLE;
            case JUNGLE -> PLAINS;
            case PLAINS -> SAVANNA;
            case SAVANNA -> SNOW;
            case SNOW -> SWAMP;
            case SWAMP -> TAIGA;
            case TAIGA -> DESERT;
        };
    }

    public Item getRepresentationItem() {
        return switch(this) {
            case DESERT -> Items.SAND;
            case JUNGLE -> Items.COCOA_BEANS;
            case PLAINS -> Items.GRASS_BLOCK;
            case SAVANNA -> Items.ACACIA_SAPLING;
            case SNOW -> Items.SNOWBALL;
            case SWAMP -> Items.LILY_PAD;
            case TAIGA -> Items.SWEET_BERRIES;
        };
    }
}
