package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.AllayEntity;

public class EquipmentCustomizationManager {

    public static <E extends Entity> EquipmentCustomization<E> getEquipmentOptions(E entity) {
        EquipmentCustomization heldItemCustomization = switch (entity) {
            case AllayEntity ae -> new AllayCustomizations();
            case EndermanEntity ee -> new EndermanCustomization();

            default -> null;
        };


        return heldItemCustomization;

    }

}
