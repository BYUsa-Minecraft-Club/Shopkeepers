package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.AllayEntity;

import java.util.ArrayList;
import java.util.List;

public class EquipmentCustomizationManager {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E extends Entity> List<EquipmentCustomization<E>> getEquipmentOptions(E entity) {
        List<EquipmentCustomization<E>> equipmentOptions = new ArrayList<>();
        EquipmentCustomization heldItemCustomization = switch (entity) {
            case AllayEntity ae -> new AllayCustomizations();
            case EndermanEntity ee -> new EndermanCustomization();

            default -> null;
        };

        if(heldItemCustomization != null) {
            equipmentOptions.add(heldItemCustomization);
        }

        return equipmentOptions;

    }

}
