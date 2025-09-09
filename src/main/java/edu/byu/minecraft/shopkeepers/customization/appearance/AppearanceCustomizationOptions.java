package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import java.util.List;

public record AppearanceCustomizationOptions<E extends Entity>(Item mobSpawnEgg,
                                                               List<AppearanceCustomization<E>> customizations,
                                                               HeldItemCustomization heldItemCustomization,
                                                               String mobName) {}
