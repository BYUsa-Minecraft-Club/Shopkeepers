package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.BeeEntityVariationSetter;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BeeCustomizations {

    public static List<ShopkeeperCustomization<BeeEntity>> getBeeCustomizations(BeeEntity bee) {
        List<ShopkeeperCustomization<BeeEntity>> customizations = new ArrayList<>();
        customizations.add(new BeeNectarCustomization(bee.hasNectar()));
        customizations.add(new BeeStingerCustomization(!bee.hasStung()));
        customizations.add(new AngerableCustomization<>(bee));
        return customizations;
    }

    private record BeeNectarCustomization(boolean hasNectar) implements ShopkeeperCustomization<BeeEntity> {

        @Override
        public String customizationDescription() {
            return "Nectar";
        }

        @Override
        public String currentDescription() {
            return hasNectar ? "Has Nectar" : "No Nectar";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasNectar ? Items.WILDFLOWERS : Items.RED_DYE;
        }

        @Override
        public BeeNectarCustomization setNext(BeeEntity shopkeeper) {
            boolean hasNectar = shopkeeper.hasNectar();
            ((BeeEntityVariationSetter) shopkeeper).invokeSetHasNectar(!hasNectar);
            return new BeeNectarCustomization(!hasNectar);
        }
    }

    private record BeeStingerCustomization(boolean hasStinger) implements ShopkeeperCustomization<BeeEntity> {

        @Override
        public String customizationDescription() {
            return "Stinger";
        }

        @Override
        public String currentDescription() {
            return hasStinger ? "Has Stinger" : "No Stinger";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasStinger ? Items.LIME_DYE : Items.RED_DYE;
        }

        @Override
        public BeeStingerCustomization setNext(BeeEntity shopkeeper) {
            boolean hasStung = shopkeeper.hasStung();
            ((BeeEntityVariationSetter) shopkeeper).invokeSetHasStung(!hasStung);
            return new BeeStingerCustomization(hasStung);
        }
    }

}
