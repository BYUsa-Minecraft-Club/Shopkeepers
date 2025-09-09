package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class PandaCustomizations {

    public static List<AppearanceCustomization<PandaEntity>> getPandaCustomizations(PandaEntity panda) {
        List<AppearanceCustomization<PandaEntity>> customizations = new ArrayList<>();
        customizations.add(new PandaGeneCustomization(panda.getMainGene()));
        return customizations;
    }

    private record PandaGeneCustomization(PandaEntity.Gene gene)
            implements AppearanceCustomization<PandaEntity> {

        @Override
        public String customizationDescription() {
            return "Panda Gene";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(gene.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (gene) {
                case NORMAL -> Items.BAMBOO;
                case LAZY -> Items.BLACK_BED;
                case WORRIED -> Items.FOX_SPAWN_EGG;
                case PLAYFUL -> Items.BAMBOO_CHEST_RAFT;
                case BROWN -> Items.BROWN_DYE;
                case WEAK -> Items.LEATHER_HORSE_ARMOR;
                case AGGRESSIVE -> Items.IRON_SWORD;
            };
        }

        @Override
        public AppearanceCustomization<PandaEntity> setNext(PandaEntity shopkeeper) {
            PandaEntity.Gene next = CustomizationUtils.nextAlphabetically(gene, PandaEntity.Gene.values());
            shopkeeper.setMainGene(next);
            shopkeeper.setHiddenGene(next);
            return new PandaGeneCustomization(next);
        }
    }
}
