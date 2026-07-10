package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PandaCustomizations {

    public static List<AppearanceCustomization<Panda>> getPandaCustomizations(Panda panda) {
        List<AppearanceCustomization<Panda>> customizations = new ArrayList<>();
        customizations.add(new PandaGeneCustomization(panda.getMainGene()));
        return customizations;
    }

    private record PandaGeneCustomization(Panda.Gene gene)
            implements AppearanceCustomization<Panda> {

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
                case LAZY -> Items.BED.black();
                case WORRIED -> Items.FOX_SPAWN_EGG;
                case PLAYFUL -> Items.BAMBOO_CHEST_RAFT;
                case BROWN -> Items.DYE.brown();
                case WEAK -> Items.LEATHER_HORSE_ARMOR;
                case AGGRESSIVE -> Items.IRON_SWORD;
            };
        }

        @Override
        public AppearanceCustomization<Panda> setNext(Panda shopkeeper) {
            Panda.Gene next = CustomizationUtils.nextEnum(gene, Panda.Gene.values());
            shopkeeper.setMainGene(next);
            shopkeeper.setHiddenGene(next);
            return new PandaGeneCustomization(next);
        }
    }
}
