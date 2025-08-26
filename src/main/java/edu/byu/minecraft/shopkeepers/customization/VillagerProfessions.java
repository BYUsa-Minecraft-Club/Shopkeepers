package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.VillagerProfession;

public enum VillagerProfessions implements ShopkeeperCustomization<VillagerEntity, RegistryKey<VillagerProfession>> {
    NONE(VillagerProfession.NONE),
    ARMORER(VillagerProfession.ARMORER),
    BUTCHER(VillagerProfession.BUTCHER),
    CARTOGRAPHER(VillagerProfession.CARTOGRAPHER),
    CLERIC(VillagerProfession.CLERIC),
    FARMER(VillagerProfession.FARMER),
    FISHERMAN(VillagerProfession.FISHERMAN),
    FLETCHER(VillagerProfession.FLETCHER),
    LEATHERWORKER(VillagerProfession.LEATHERWORKER),
    LIBRARIAN(VillagerProfession.LIBRARIAN),
    MASON(VillagerProfession.MASON),
    NITWIT(VillagerProfession.NITWIT),
    SHEPHERD(VillagerProfession.SHEPHERD),
    TOOLSMITH(VillagerProfession.TOOLSMITH),
    WEAPONSMITH(VillagerProfession.WEAPONSMITH);

    private final RegistryKey<VillagerProfession> profession;
    private VillagerProfessions(RegistryKey<VillagerProfession> Profession) {
        this.profession = Profession;
    }

    public static VillagerProfessions getVillagerProfession(RegistryKey<VillagerProfession> profession) {
        for(VillagerProfessions vt : VillagerProfessions.values()) {
            if(vt.profession == profession) {
                return vt;
            }
        }
        throw new IllegalArgumentException("No VillagerProfessions found for " + profession);
    }

    @Override
    public String customizationDescription() {
        return "Villager Profession";
    }

    @Override
    public RegistryKey<VillagerProfession> getCurrent(VillagerEntity shopkeeper) {
        return shopkeeper.getVillagerData().profession().getKey().get();
    }

    @Override
    public String currentDescription(RegistryKey<VillagerProfession> villagerProfessionRegistryKey) {
        return villagerProfessionRegistryKey.getValue().getPath();
    }

    @Override
    public RegistryKey<VillagerProfession> getNext(RegistryKey<VillagerProfession> villagerProfessionRegistryKey) {
        return getVillagerProfession(villagerProfessionRegistryKey).getNextInCycle().getProfession();
    }

    @Override
    public void setCurrent(VillagerEntity shopkeeper, RegistryKey<VillagerProfession> villagerProfessionRegistryKey) {
        shopkeeper.setVillagerData(shopkeeper.getVillagerData().withProfession(shopkeeper.getRegistryManager(), villagerProfessionRegistryKey));
    }

    public RegistryKey<VillagerProfession> getProfession() {
        return profession;
    }

    public VillagerProfessions getNextInCycle() {
        return switch (this) {
            case NONE -> NITWIT;
            case NITWIT -> ARMORER;
            case ARMORER -> BUTCHER;
            case BUTCHER -> CARTOGRAPHER;
            case CARTOGRAPHER -> CLERIC;
            case CLERIC -> FARMER;
            case FARMER -> FISHERMAN;
            case FISHERMAN -> FLETCHER;
            case FLETCHER -> LEATHERWORKER;
            case LEATHERWORKER -> LIBRARIAN;
            case LIBRARIAN -> MASON;
            case MASON -> SHEPHERD;
            case SHEPHERD -> TOOLSMITH;
            case TOOLSMITH -> WEAPONSMITH;
            case WEAPONSMITH -> NONE;
        };
    }

    public Item getRepresentationItem() {
        return switch(this) {
            case NONE -> Items.RED_DYE;
            case NITWIT -> Items.GREEN_DYE;
            case ARMORER -> Items.BLAST_FURNACE;
            case BUTCHER -> Items.SMOKER;
            case CARTOGRAPHER -> Items.CARTOGRAPHY_TABLE;
            case CLERIC -> Items.BREWING_STAND;
            case FARMER -> Items.COMPOSTER;
            case FISHERMAN -> Items.BARREL;
            case FLETCHER -> Items.FLETCHING_TABLE;
            case LEATHERWORKER -> Items.CAULDRON;
            case LIBRARIAN -> Items.LECTERN;
            case MASON -> Items.STONECUTTER;
            case SHEPHERD -> Items.LOOM;
            case TOOLSMITH -> Items.SMITHING_TABLE;
            case WEAPONSMITH -> Items.GRINDSTONE;
        };
    }
}
