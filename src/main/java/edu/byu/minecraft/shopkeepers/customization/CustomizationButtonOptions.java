package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.gui.MobSettingsGui;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import java.util.List;

public record CustomizationButtonOptions<E extends Entity>(Item mobSpawnEgg, List<ShopkeeperCustomization<E>> customizations, String mobName) {}
