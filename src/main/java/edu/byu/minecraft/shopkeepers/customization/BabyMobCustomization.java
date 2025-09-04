package edu.byu.minecraft.shopkeepers.customization;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public record BabyMobCustomization<E extends MobEntity>(boolean isBaby) implements ShopkeeperCustomization<E> {

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
    public ShopkeeperCustomization<E> setNext(E shopkeeper) {
        shopkeeper.setBaby(!isBaby);
        if(!isBaby && shopkeeper instanceof PassiveEntity pe) {
            pe.setBreedingAge(-2099999999); //about 3.4 years of loaded time
        }
        return new BabyMobCustomization<>(!isBaby);
    }
}
