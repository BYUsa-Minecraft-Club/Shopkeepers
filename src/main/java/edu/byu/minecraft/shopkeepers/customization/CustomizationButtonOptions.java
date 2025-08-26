package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.gui.MobSettingsGui;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public record CustomizationButtonOptions<E extends Entity>(Item mobSpawnEgg, MobSettingsGui<E> settingsGui, String mobName) {}
