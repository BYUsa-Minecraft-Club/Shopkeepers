package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AngerableCustomization<E extends Entity & Angerable> implements AppearanceCustomization<E> {

    private final boolean isAngry;

    AngerableCustomization(Angerable angerable) {
        this(angerable.hasAngerTime());
    }

    AngerableCustomization(boolean isAngry) {
        this.isAngry = isAngry;
    }

    @Override
    public String customizationDescription() {
        return "Angry";
    }

    @Override
    public String currentDescription() {
        return isAngry ? "Angry" : "Not Angry";
    }

    @Override
    public Item getCurrentRepresentationItem() {
        return isAngry ? Items.LIME_DYE : Items.RED_DYE;
    }

    @Override
    public AppearanceCustomization<E> setNext(E shopkeeper) {
        shopkeeper.stopAnger();
        if (!isAngry) {
            shopkeeper.setAngerDuration(2099999999);
        }
        return new AngerableCustomization<>(!isAngry);
    }

}
