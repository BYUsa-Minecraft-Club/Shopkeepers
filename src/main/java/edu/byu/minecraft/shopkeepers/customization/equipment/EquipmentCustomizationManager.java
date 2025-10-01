package edu.byu.minecraft.shopkeepers.customization.equipment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;

import java.util.ArrayList;
import java.util.List;

public class EquipmentCustomizationManager {

    @SuppressWarnings({"unchecked"})
    public static <E extends LivingEntity> List<EquipmentCustomization<E>> getEquipmentOptions(E entity) {
        List<EquipmentCustomization<E>> equipmentOptions = new ArrayList<>();

        ArmorCustomizations.addArmorCustomizations(entity, equipmentOptions);
        BodyArmorCustomization.addBodyArmorCustomization(entity, equipmentOptions);
        HeldItemCustomizations.addHeldItemCustomizations(entity, equipmentOptions);
        SaddleCustomization.addSaddleCustomization(entity, equipmentOptions);

        if(entity instanceof EndermanEntity enderman) {
            equipmentOptions.add((EquipmentCustomization<E>) new EndermanCustomization(enderman));
        }

        return equipmentOptions;

    }

}
