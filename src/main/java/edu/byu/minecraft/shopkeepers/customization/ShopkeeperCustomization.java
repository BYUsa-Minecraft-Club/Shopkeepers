package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.Entity;

public interface ShopkeeperCustomization<E extends Entity, T> {
    String customizationDescription();
    T getCurrent(E shopkeeper);
    String currentDescription(T t);
    T getNext(T t);
    void setCurrent(E shopkeeper, T t);
}
