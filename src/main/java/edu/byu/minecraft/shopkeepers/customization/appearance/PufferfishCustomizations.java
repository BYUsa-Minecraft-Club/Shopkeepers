package edu.byu.minecraft.shopkeepers.customization.appearance;


import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PufferfishCustomizations {
    public static List<AppearanceCustomization<Pufferfish>> getPufferfishCustomizations(Pufferfish entity) {
        List<AppearanceCustomization<Pufferfish>> customizations = new ArrayList<>();
        customizations.add(new PufferfishSizeCustomization(entity.getPuffState()));
        return customizations;
    }

    private record PufferfishSizeCustomization(int size) implements AppearanceCustomization<Pufferfish> {

        @Override
        public String customizationDescription() {
            return "Puffed Size";
        }

        @Override
        public String currentDescription() {
            return switch (size) {
                case 0 -> "Small/Deflated";
                case 1 -> "Medium/Half Puffed";
                case 2 -> "Large/Fully Puffed";
                default -> "Unobtainable";
            };
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (size) {
                case 0 -> Items.WOODEN_SWORD;
                case 1 -> Items.IRON_SWORD;
                case 2 -> Items.NETHERITE_SWORD;
                default -> Items.BARRIER;
            };
        }

        @Override
        public AppearanceCustomization<Pufferfish> setNext(Pufferfish shopkeeper) {
            int next = (size + 1) % 3;
            shopkeeper.setPuffState(next);
            return new PufferfishSizeCustomization(next);
        }
    }

}
