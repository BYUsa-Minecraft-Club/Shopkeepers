package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.passive.Cracks;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class IronGolemCustomizations {

    public static List<ShopkeeperCustomization<IronGolemEntity>> getIronGolemCustomizations(IronGolemEntity ironGolem) {
        List<ShopkeeperCustomization<IronGolemEntity>> customizations = new ArrayList<>();
        customizations.add(new IronGolemCrackCustomization(ironGolem.getCrackLevel()));
        return customizations;
    }

    private record IronGolemCrackCustomization(Cracks.CrackLevel crackLevel)
            implements ShopkeeperCustomization<IronGolemEntity> {

        @Override
        public String customizationDescription() {
            return "Crack Level";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(crackLevel.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (crackLevel) {
                case NONE -> Items.LIME_DYE;
                case LOW -> Items.YELLOW_DYE;
                case MEDIUM -> Items.ORANGE_DYE;
                case HIGH -> Items.RED_DYE;
            };
        }

        @Override
        public ShopkeeperCustomization<IronGolemEntity> setNext(IronGolemEntity shopkeeper) {
            var next = CustomizationUtils.nextAlphabetically(crackLevel, Cracks.CrackLevel.values());
            shopkeeper.setHealth(switch (next) {
                case NONE -> 80.0F;
                case LOW -> 60.0F;
                case MEDIUM -> 40.0F;
                case HIGH -> 20.0F;
            });
            return new IronGolemCrackCustomization(next);
        }
    }
}
