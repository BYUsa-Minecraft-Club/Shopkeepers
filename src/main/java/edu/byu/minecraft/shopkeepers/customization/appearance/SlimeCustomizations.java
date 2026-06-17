package edu.byu.minecraft.shopkeepers.customization.appearance;


import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SlimeCustomizations {
    public static List<AppearanceCustomization<Slime>> getSlimeCustomizations(Slime entity) {
        List<AppearanceCustomization<Slime>> customizations = new ArrayList<>();
        customizations.add(new SlimeSizeCustomization(entity.getSize()));
        return customizations;
    }

    private record SlimeSizeCustomization(int size) implements AppearanceCustomization<Slime> {

        @Override
        public String customizationDescription() {
            return "Size";
        }

        @Override
        public String currentDescription() {
            return switch (size) {
                case 1 -> "Small";
                case 2 -> "Medium";
                case 3 -> "Semi-Large";
                case 4 -> "Big";
                default -> "Unobtainable";
            };
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (size) {
                case 1 -> Items.SLIME_BALL;
                case 2 -> Items.MAGMA_CREAM;
                case 3 -> Items.SLIME_BLOCK;
                case 4 -> Items.MAGMA_BLOCK;
                default -> Items.BARRIER;
            };
        }

        @Override
        public AppearanceCustomization<Slime> setNext(Slime shopkeeper) {
            int next = (size % 4) + 1;
            shopkeeper.setSize(next, true);
            return new SlimeSizeCustomization(next);
        }
    }

}
