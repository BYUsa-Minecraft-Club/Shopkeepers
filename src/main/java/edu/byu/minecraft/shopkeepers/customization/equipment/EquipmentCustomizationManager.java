package edu.byu.minecraft.shopkeepers.customization.equipment;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;

public class EquipmentCustomizationManager {

    @SuppressWarnings({"unchecked"})
    public static <E extends LivingEntity> List<EquipmentCustomization<E>> getEquipmentOptions(E entity) {
        List<EquipmentCustomization<E>> equipmentOptions = new ArrayList<>();

        ArmorCustomizations.addArmorCustomizations(entity, equipmentOptions);
        BodyArmorCustomization.addBodyArmorCustomization(entity, equipmentOptions);
        HeldItemCustomizations.addHeldItemCustomizations(entity, equipmentOptions);
        SaddleCustomization.addSaddleCustomization(entity, equipmentOptions);

        if(entity instanceof EnderMan enderman) {
            equipmentOptions.add((EquipmentCustomization<E>) new EndermanCustomization(enderman));
        }

        return equipmentOptions;

    }

}
