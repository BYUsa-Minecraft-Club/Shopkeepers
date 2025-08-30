package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.gui.MobSettingsGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class CustomizationManager {
    public static <E extends Entity> CustomizationButtonOptions<?> getCustomizationButtonOptions(E entity, ServerPlayerEntity player, SimpleGui guiParent) {
        if (entity instanceof VillagerEntity ve) {
            return new CustomizationButtonOptions<>(Items.VILLAGER_SPAWN_EGG,
                    new MobSettingsGui<>(player, ve, VillagerCustomizations.getVillagerCustomizations(ve), guiParent),
                    "Villager");
        }
        if (entity instanceof RabbitEntity re) {
            return new CustomizationButtonOptions<>(Items.RABBIT_SPAWN_EGG,
                    new MobSettingsGui<>(player, re, RabbitCustomizations.getRabbitCustomizations(re), guiParent),
                    "Rabbit");
        }
        return null;
    }
}
