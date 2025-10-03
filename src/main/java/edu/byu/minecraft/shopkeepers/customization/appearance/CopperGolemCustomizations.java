package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.mixin.invoker.CopperGolemOxidationTimerSetter;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.CopperGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CopperGolemCustomizations {

    public static List<AppearanceCustomization<CopperGolemEntity>> getCopperGolemCustomizations(CopperGolemEntity golem) {
        List<AppearanceCustomization<CopperGolemEntity>> customizations = new ArrayList<>();
        customizations.add(new CopperGolemOxidationCustomization(golem.getOxidationLevel()));
        customizations.add(CopperGolemPoppyCustomization.forGolem(golem));
        return customizations;
    }

    private record CopperGolemOxidationCustomization(Oxidizable.OxidationLevel level)
            implements AppearanceCustomization<CopperGolemEntity> {

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
                case UNAFFECTED -> Items.COPPER_BLOCK;
                case EXPOSED -> Items.EXPOSED_COPPER;
                case WEATHERED -> Items.WEATHERED_COPPER;
                case OXIDIZED -> Items.OXIDIZED_COPPER;
            };
        }

        @Override
        public AppearanceCustomization<CopperGolemEntity> setNext(CopperGolemEntity shopkeeper) {
            Oxidizable.OxidationLevel next = CustomizationUtils.nextEnum(level, Oxidizable.OxidationLevel.values());
            shopkeeper.setOxidationLevel(next);
            ((CopperGolemOxidationTimerSetter) (Object) shopkeeper).setNextOxidationAge(-2L);
            return new CopperGolemOxidationCustomization(next);
        }
    }

    private record CopperGolemPoppyCustomization(boolean hasPoppy) implements AppearanceCustomization<CopperGolemEntity> {

        public static CopperGolemPoppyCustomization forGolem(CopperGolemEntity golem) {
            ItemStack saddleStack = golem.getEquippedStack(EquipmentSlot.SADDLE);
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
        public CopperGolemPoppyCustomization setNext(CopperGolemEntity shopkeeper) {
            shopkeeper.equipStack(EquipmentSlot.SADDLE, hasPoppy ? ItemStack.EMPTY : new ItemStack(Items.POPPY));
            return new CopperGolemPoppyCustomization(!hasPoppy);
        }
    }
}
