package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.gui.MobSettingsGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.function.Function;

public class CustomizationManager {

    public static <E extends Entity> CustomizationButtonOptions<?> getCustomizationButtonOptions(E entity, ServerPlayerEntity player, SimpleGui guiParent) {

        //specific types
        if (entity instanceof VillagerEntity ve) {
            return options(ve, player, VillagerCustomizations::getVillagerCustomizations, guiParent);
        }
        if (entity instanceof ZombieVillagerEntity zve) {
            return options(zve, player, VillagerCustomizations::getVillagerCustomizations, guiParent);
        }
        if (entity instanceof RabbitEntity re) {
            return options(re, player, RabbitCustomizations::getRabbitCustomizations, guiParent);
        }
        if (entity instanceof ParrotEntity pe) {
            return options(pe, player, ParrotCustomizations::getParrotCustomizations, guiParent);
        }
        if(entity instanceof WolfEntity we) {
            return options(we, player, WolfCustomizations::getWolfCustomizations, guiParent);
        }
        if(entity instanceof CatEntity ce) {
            return options(ce, player, CatCustomizations::getCatCustomizations, guiParent);
        }


        //super types
        if (entity instanceof TameableEntity te) {
            return options(te, player, TameableMobCustomizations::getTameableCustomizations, guiParent);
        }

        return null;
    }

    private static <E extends Entity> CustomizationButtonOptions<E> options(E entity, ServerPlayerEntity player,
                                                                            Function<E, List<ShopkeeperCustomization<E>>> customizations,
                                                                            SimpleGui guiParent) {
        return new CustomizationButtonOptions<>(SpawnEggItem.forEntity(entity.getType()),
                new MobSettingsGui<>(player, entity, customizations.apply(entity), guiParent),
                CustomizationUtils.capitalize(entity.getType().getName().getString()));
    }
}
