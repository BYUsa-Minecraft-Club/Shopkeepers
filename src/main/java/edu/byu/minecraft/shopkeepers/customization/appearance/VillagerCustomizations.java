package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class VillagerCustomizations {
    public static <E extends Entity & VillagerDataHolder> List<AppearanceCustomization<E>> getVillagerCustomizations(E villagerDataContainerEntity) {
        List<AppearanceCustomization<E>> customizations = new ArrayList<>();
        customizations.add(VillagerProfessions.getCurrentprofession(villagerDataContainerEntity));
        customizations.add(VillagerTypes.getCurrentType(villagerDataContainerEntity));
        customizations.add(VillagerLevels.getCurrentLevel(villagerDataContainerEntity));
        return customizations;
    }

    private record VillagerProfessions<E extends Entity & VillagerDataHolder>(Professions profession) implements
            AppearanceCustomization<E> {
        private enum Professions {
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

            private final ResourceKey<VillagerProfession> profession;

            private Professions(ResourceKey<VillagerProfession> Profession) {
                this.profession = Profession;
            }
        }

        public static <E extends Entity & VillagerDataHolder> VillagerProfessions<E> getCurrentprofession(VillagerDataHolder shopkeeper) {
            ResourceKey<VillagerProfession> profession =
                    shopkeeper.getVillagerData().profession().unwrapKey().orElseThrow();
            for(Professions vt : Professions.values()) {
                if(vt.profession == profession) {
                    return new VillagerProfessions<>(vt);
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
            return CustomizationUtils.capitalize(profession.profession.identifier().getPath());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch(profession) {
                case NONE -> Items.DYE.red();
                case NITWIT -> Items.DYE.green();
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
        public VillagerProfessions<E> setNext(E shopkeeper) {
            Professions next =
                    Professions.values()[(profession.ordinal() + 1) % Professions.values().length];
            shopkeeper.setVillagerData(shopkeeper.getVillagerData().withProfession(shopkeeper.registryAccess(), next.profession));
            return new VillagerProfessions<>(next);
        }
    }

    private record VillagerTypes<E extends Entity & VillagerDataHolder>(Types type) implements
            AppearanceCustomization<E> {

            private enum Types {
                DESERT(VillagerType.DESERT),
                JUNGLE(VillagerType.JUNGLE),
                PLAINS(VillagerType.PLAINS),
                SAVANNA(VillagerType.SAVANNA),
                SNOW(VillagerType.SNOW),
                SWAMP(VillagerType.SWAMP),
                TAIGA(VillagerType.TAIGA);

                private final ResourceKey<VillagerType> type;

                private Types(ResourceKey<VillagerType> type) {
                    this.type = type;
                }
            }

        public static <E extends Entity & VillagerDataHolder> VillagerTypes<E> getCurrentType(E shopkeeper) {
                ResourceKey<VillagerType> type = shopkeeper.getVillagerData().type().unwrapKey().orElseThrow();
                for (Types vt : Types.values()) {
                    if (vt.type == type) {
                        return new VillagerTypes<>(vt);
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
                return CustomizationUtils.capitalize(type.type.identifier().getPath());
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
            public VillagerTypes<E> setNext(E shopkeeper) {
                Types next = CustomizationUtils.nextEnum(type, Types.values());
                shopkeeper.setVillagerData(
                        shopkeeper.getVillagerData().withType(shopkeeper.registryAccess(), next.type));
                return new VillagerTypes<>(next);
            }

    }

    private static class VillagerLevels<E extends Entity & VillagerDataHolder> implements
            AppearanceCustomization<E> {

        int level;
        private VillagerLevels(int level) {
            this.level = level;
        }

        public static <E extends Entity & VillagerDataHolder> VillagerLevels<E> getCurrentLevel(E shopkeeper) {
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
