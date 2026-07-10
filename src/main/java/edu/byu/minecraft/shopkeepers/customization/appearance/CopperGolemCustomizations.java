package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.CopperGolemOxidationTimerSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopper;

public class CopperGolemCustomizations {

    public static List<AppearanceCustomization<CopperGolem>> getCopperGolemCustomizations(CopperGolem golem) {
        List<AppearanceCustomization<CopperGolem>> customizations = new ArrayList<>();
        customizations.add(new CopperGolemOxidationCustomization(golem.getWeatherState()));
        customizations.add(CopperGolemPoppyCustomization.forGolem(golem));
        return customizations;
    }

    private record CopperGolemOxidationCustomization(WeatheringCopper.WeatherState level)
            implements AppearanceCustomization<CopperGolem> {

        @Override
        public String customizationDescription() {
            return "Oxidation Level";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(level.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (level) {
                case UNAFFECTED -> Items.COPPER_BLOCK.weathering().unaffected();
                case EXPOSED -> Items.COPPER_BLOCK.weathering().exposed();
                case WEATHERED -> Items.COPPER_BLOCK.weathering().weathered();
                case OXIDIZED -> Items.COPPER_BLOCK.weathering().oxidized();
            };
        }

        @Override
        public AppearanceCustomization<CopperGolem> setNext(CopperGolem shopkeeper) {
            WeatheringCopper.WeatherState next = CustomizationUtils.nextEnum(level, WeatheringCopper.WeatherState.values());
            shopkeeper.setWeatherState(next);
            ((CopperGolemOxidationTimerSetter) (Object) shopkeeper).setNextOxidationAge(-2L);
            return new CopperGolemOxidationCustomization(next);
        }
    }

    private record CopperGolemPoppyCustomization(boolean hasPoppy) implements AppearanceCustomization<CopperGolem> {

        public static CopperGolemPoppyCustomization forGolem(CopperGolem golem) {
            ItemStack saddleStack = golem.getItemBySlot(EquipmentSlot.SADDLE);
            return new CopperGolemPoppyCustomization(saddleStack != null && !saddleStack.isEmpty());
        }

        @Override
        public String customizationDescription() {
            return "Poppy";
        }

        @Override
        public String currentDescription() {
            return hasPoppy ? "Has Poppy" : "No Poppy";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return hasPoppy ? Items.POPPY : Items.BARRIER;
        }

        @Override
        public CopperGolemPoppyCustomization setNext(CopperGolem shopkeeper) {
            shopkeeper.setItemSlot(EquipmentSlot.SADDLE, hasPoppy ? ItemStack.EMPTY : new ItemStack(Items.POPPY));
            return new CopperGolemPoppyCustomization(!hasPoppy);
        }
    }
}
