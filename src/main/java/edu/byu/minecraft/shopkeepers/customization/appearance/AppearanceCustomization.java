package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public interface AppearanceCustomization<E extends Entity> {
    String customizationDescription();
    String currentDescription();
    Item getCurrentRepresentationItem();
    AppearanceCustomization<E> setNext(E shopkeeper);
}
