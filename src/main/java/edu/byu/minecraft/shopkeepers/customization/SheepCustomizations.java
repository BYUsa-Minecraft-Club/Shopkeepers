package edu.byu.minecraft.shopkeepers.customization;


import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class SheepCustomizations {
    public static List<ShopkeeperCustomization<SheepEntity>> getSheepCustomizations(SheepEntity entity) {
        List<ShopkeeperCustomization<SheepEntity>> customizations = new ArrayList<>();
        customizations.add(new SheepShearedCustomization(entity.isSheared()));
        customizations.add(new SheepColorCustomization(entity.getColor()));
        return customizations;
    }

    private record SheepShearedCustomization(boolean isSheared)
            implements ShopkeeperCustomization<SheepEntity> {

        @Override
        public String customizationDescription() {
            return "Sheared";
        }

        @Override
        public String currentDescription() {
            return isSheared ? "Sheared" : "Not Sheared";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isSheared ? Items.SHEARS : Items.WHITE_WOOL;
        }

        @Override
        public ShopkeeperCustomization<SheepEntity> setNext(SheepEntity shopkeeper) {
            boolean isSheared = shopkeeper.isSheared();
            shopkeeper.setSheared(!isSheared);
            return new SheepShearedCustomization(!isSheared);
        }
    }
    
    private record SheepColorCustomization(DyeColor color) implements ShopkeeperCustomization<SheepEntity> {

        @Override
        public String customizationDescription() {
            return "Wool Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(color.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return CustomizationUtils.getDyeItem(color);
        }

        @Override
        public ShopkeeperCustomization<SheepEntity> setNext(SheepEntity shopkeeper) {
            DyeColor next = CustomizationUtils.nextAlphabetically(shopkeeper.getColor(), DyeColor.values());
            shopkeeper.setColor(next);
            return new SheepColorCustomization(next);
        }
    }
}
