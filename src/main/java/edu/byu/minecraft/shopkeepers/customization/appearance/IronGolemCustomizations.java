package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class IronGolemCustomizations {

    public static List<AppearanceCustomization<IronGolem>> getIronGolemCustomizations(IronGolem ironGolem) {
        List<AppearanceCustomization<IronGolem>> customizations = new ArrayList<>();
        customizations.add(new IronGolemCrackCustomization(ironGolem.getCrackiness()));
        return customizations;
    }

    private record IronGolemCrackCustomization(Crackiness.Level crackLevel)
            implements AppearanceCustomization<IronGolem> {

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
                case NONE -> Items.DYE.lime();
                case LOW -> Items.DYE.yellow();
                case MEDIUM -> Items.DYE.orange();
                case HIGH -> Items.DYE.red();
            };
        }

        @Override
        public AppearanceCustomization<IronGolem> setNext(IronGolem shopkeeper) {
            var next = CustomizationUtils.nextEnum(crackLevel, Crackiness.Level.values());
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
