package edu.byu.minecraft.shopkeepers.customization.appearance;


import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SheepCustomizations {
    public static List<AppearanceCustomization<Sheep>> getSheepCustomizations(Sheep entity) {
        List<AppearanceCustomization<Sheep>> customizations = new ArrayList<>();
        customizations.add(new SheepShearedCustomization(entity.isSheared()));
        customizations.add(new SheepColorCustomization(entity.getColor()));
        return customizations;
    }

    private record SheepShearedCustomization(boolean isSheared)
            implements AppearanceCustomization<Sheep> {

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
            return isSheared ? Items.SHEARS : Items.WOOL.white();
        }

        @Override
        public AppearanceCustomization<Sheep> setNext(Sheep shopkeeper) {
            shopkeeper.setSheared(!isSheared);
            return new SheepShearedCustomization(!isSheared);
        }
    }
    
    private record SheepColorCustomization(DyeColor color) implements AppearanceCustomization<Sheep> {

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
        public AppearanceCustomization<Sheep> setNext(Sheep shopkeeper) {
            DyeColor next = CustomizationUtils.nextInOrder(color);
            shopkeeper.setColor(next);
            return new SheepColorCustomization(next);
        }
    }
}
