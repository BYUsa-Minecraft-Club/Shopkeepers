package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AngerableCustomization<E extends Entity & NeutralMob> implements AppearanceCustomization<E> {

    private final boolean isAngry;

    AngerableCustomization(NeutralMob angerable) {
        this(angerable.isAngry());
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
        shopkeeper.stopBeingAngry();
        if (!isAngry) {
            shopkeeper.setTimeToRemainAngry(2099999999);
        }
        return new AngerableCustomization<>(!isAngry);
    }

}
