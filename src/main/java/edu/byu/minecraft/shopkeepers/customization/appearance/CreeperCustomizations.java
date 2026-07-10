package edu.byu.minecraft.shopkeepers.customization.appearance;


import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CreeperCustomizations {
    public static List<AppearanceCustomization<Creeper>> getCreeperCustomizations(Creeper entity) {
        List<AppearanceCustomization<Creeper>> customizations = new ArrayList<>();
        customizations.add(new CreeperChargedCustomization(entity.isPowered()));
        return customizations;
    }

    private record CreeperChargedCustomization(boolean isCharged)
            implements AppearanceCustomization<Creeper> {

        @Override
        public String customizationDescription() {
            return "Charged";
        }

        @Override
        public String currentDescription() {
            return isCharged ? "Charged" : "Not Charged";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isCharged ? Items.LIGHTNING_ROD.weathering().unaffected() : Items.CREEPER_HEAD;
        }

        @Override
        public AppearanceCustomization<Creeper> setNext(Creeper shopkeeper) {
            ((CreeperEditor) shopkeeper).shopkeepers$setCharged(!isCharged);
            return new CreeperChargedCustomization(!isCharged);
        }
    }

    public interface CreeperEditor {
        void shopkeepers$setCharged(boolean charged);
    }

}
