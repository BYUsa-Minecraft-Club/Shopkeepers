package edu.byu.minecraft.shopkeepers.customization;


import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class PufferfishCustomizations {
    public static List<ShopkeeperCustomization<PufferfishEntity>> getPufferfishCustomizations(PufferfishEntity entity) {
        List<ShopkeeperCustomization<PufferfishEntity>> customizations = new ArrayList<>();
        customizations.add(new PufferfishSizeCustomization(entity.getPuffState()));
        return customizations;
    }

    private record PufferfishSizeCustomization(int size) implements ShopkeeperCustomization<PufferfishEntity> {

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
        public ShopkeeperCustomization<PufferfishEntity> setNext(PufferfishEntity shopkeeper) {
            int next = (size + 1) % 3;
            shopkeeper.setPuffState(next);
            return new PufferfishSizeCustomization(next);
        }
    }

}
