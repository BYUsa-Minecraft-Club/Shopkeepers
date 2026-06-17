package edu.byu.minecraft.shopkeepers.customization.appearance;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record BabyMobCustomization<E extends Mob>(boolean isBaby) implements AppearanceCustomization<E> {

    @Override
    public String customizationDescription() {
        return "Baby";
    }

    @Override
    public String currentDescription() {
        return isBaby() ? "Baby" : "Adult";
    }

    @Override
    public Item getCurrentRepresentationItem() {
        return isBaby ? Items.SLIME_BALL : Items.SLIME_BLOCK;
    }

    @Override
    public AppearanceCustomization<E> setNext(E shopkeeper) {
        shopkeeper.setBaby(!isBaby);
        if(!isBaby && shopkeeper instanceof AgeableMob pe) {
            pe.setAge(-2099999999); //about 3.4 years of loaded time
        }
        return new BabyMobCustomization<>(!isBaby);
    }
}
