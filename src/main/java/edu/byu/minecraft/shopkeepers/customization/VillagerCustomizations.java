package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

import java.util.ArrayList;
import java.util.List;

public class VillagerCustomizations {
    public static <E extends Entity & VillagerDataContainer> List<ShopkeeperCustomization<E>> getVillagerCustomizations(E villagerDataContainerEntity) {
        List<ShopkeeperCustomization<E>> customizations = new ArrayList<>();
        customizations.add(VillagerProfessionsWrapper.getCurrentprofession(villagerDataContainerEntity));
        customizations.add(VillagerTypesWrapper.getCurrentType(villagerDataContainerEntity));
        customizations.add(VillagerLevels.getCurrentLevel(villagerDataContainerEntity));
        return customizations;
    }

    private record VillagerProfessionsWrapper<E extends Entity & VillagerDataContainer>(VillagerProfessions profession) implements ShopkeeperCustomization<E> {
        private enum VillagerProfessions {
            NONE(VillagerProfession.NONE),
            NITWIT(VillagerProfession.NITWIT),
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
            SHEPHERD(VillagerProfession.SHEPHERD),
            TOOLSMITH(VillagerProfession.TOOLSMITH),
            WEAPONSMITH(VillagerProfession.WEAPONSMITH);

            private final RegistryKey<VillagerProfession> profession;

            private VillagerProfessions(RegistryKey<VillagerProfession> Profession) {
                this.profession = Profession;
            }
        }

        public static <E extends Entity & VillagerDataContainer> VillagerProfessionsWrapper<E> getCurrentprofession(VillagerDataContainer shopkeeper) {
            RegistryKey<VillagerProfession> profession =
                    shopkeeper.getVillagerData().profession().getKey().orElseThrow();
            for(VillagerProfessions vt : VillagerProfessions.values()) {
                if(vt.profession == profession) {
                    return new VillagerProfessionsWrapper<>(vt);
                }
            }
            throw new IllegalArgumentException("No VillagerProfessions found for villager with profession " + profession);
        }

        @Override
        public String customizationDescription() {
            return "Villager Profession";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(profession.profession.getValue().getPath());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch(profession) {
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

        @Override
        public VillagerProfessionsWrapper<E> setNext(E shopkeeper) {
            VillagerProfessions next =
                    VillagerProfessions.values()[(profession.ordinal() + 1) % VillagerProfessions.values().length];
            shopkeeper.setVillagerData(shopkeeper.getVillagerData().withProfession(shopkeeper.getRegistryManager(), next.profession));
            return new VillagerProfessionsWrapper<>(next);
        }
    }

    private record VillagerTypesWrapper<E extends Entity & VillagerDataContainer>(VillagerTypes type) implements ShopkeeperCustomization<E> {

            private enum VillagerTypes {
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
            }

        public static <E extends Entity & VillagerDataContainer> VillagerTypesWrapper<E> getCurrentType(E shopkeeper) {
                RegistryKey<VillagerType> type = shopkeeper.getVillagerData().type().getKey().orElseThrow();
                for (VillagerTypes vt : VillagerTypes.values()) {
                    if (vt.type == type) {
                        return new VillagerTypesWrapper<>(vt);
                    }
                }
                throw new IllegalArgumentException("No VillagerTypes found for villager with type " + type);
            }

            @Override
            public String customizationDescription() {
                return "Villager Type";
            }

            @Override
            public String currentDescription() {
                return CustomizationUtils.capitalize(type.type.getValue().getPath());
            }

            @Override
            public Item getCurrentRepresentationItem() {
                return switch (type) {
                    case DESERT -> Items.SAND;
                    case JUNGLE -> Items.COCOA_BEANS;
                    case PLAINS -> Items.GRASS_BLOCK;
                    case SAVANNA -> Items.ACACIA_SAPLING;
                    case SNOW -> Items.SNOWBALL;
                    case SWAMP -> Items.LILY_PAD;
                    case TAIGA -> Items.SWEET_BERRIES;
                };
            }

            @Override
            public VillagerTypesWrapper<E> setNext(E shopkeeper) {
                VillagerTypes next = VillagerTypes.values()[(type.ordinal() + 1) % VillagerTypes.values().length];
                shopkeeper.setVillagerData(
                        shopkeeper.getVillagerData().withType(shopkeeper.getRegistryManager(), next.type));
                return new VillagerTypesWrapper<>(next);
            }

    }

    private static class VillagerLevels<E extends Entity & VillagerDataContainer> implements ShopkeeperCustomization<E> {

        int level;
        private VillagerLevels(int level) {
            this.level = level;
        }

        public static <E extends Entity & VillagerDataContainer> VillagerLevels<E> getCurrentLevel(E shopkeeper) {
            return new VillagerLevels<>(shopkeeper.getVillagerData().level());
        }

        @Override
        public String customizationDescription() {
            return "Villager Level";
        }

        @Override
        public String currentDescription() {
            return switch (level) {
                case 1 -> "Novice";
                case 2 -> "Apprentice";
                case 3 -> "Journeyman";
                case 4 -> "Expert";
                case 5 -> "Master";
                default -> throw new IllegalStateException("Unexpected value: " + level);
            };
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (level) {
                case 1 -> Items.COBBLESTONE;
                case 2 -> Items.IRON_INGOT;
                case 3 -> Items.GOLD_INGOT;
                case 4 -> Items.EMERALD;
                case 5 -> Items.DIAMOND;
                default -> throw new IllegalStateException("Unexpected value: " + level);
            };
        }

        @Override
        public VillagerLevels<E> setNext(E shopkeeper) {
            int nextLevel = (level % 5) + 1;
            shopkeeper.setVillagerData(shopkeeper.getVillagerData().withLevel(nextLevel));
            return new VillagerLevels<>(nextLevel);
        }
    }
}
