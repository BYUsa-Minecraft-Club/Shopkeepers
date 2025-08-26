package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public interface ShopkeeperCustomization<E extends Entity> {
    String customizationDescription();
    String currentDescription();
    Item getCurrentRepresentationItem();
    ShopkeeperCustomization<E> setNext(E shopkeeper);
}
