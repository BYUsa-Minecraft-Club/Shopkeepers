package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public interface AppearanceCustomization<E extends Entity> {
    String customizationDescription();
    String currentDescription();
    Item getCurrentRepresentationItem();
    AppearanceCustomization<E> setNext(E shopkeeper);
}
