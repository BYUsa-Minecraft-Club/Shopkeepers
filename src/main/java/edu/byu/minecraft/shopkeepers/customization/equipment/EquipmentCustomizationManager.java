package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.AllayEntity;

public class EquipmentCustomizationManager {

    public static <E extends Entity> HeldItemCustomization getEquipmentOptions(E entity) {
        HeldItemCustomization heldItemCustomization = switch (entity) {
            case AllayEntity ae -> AllayCustomizations.getHeldItemCustomization(ae);
            case EndermanEntity ee -> EndermanCustomization.getHeldItemCustomization(ee);

            default -> null;
        };


        return heldItemCustomization;

    }

}
