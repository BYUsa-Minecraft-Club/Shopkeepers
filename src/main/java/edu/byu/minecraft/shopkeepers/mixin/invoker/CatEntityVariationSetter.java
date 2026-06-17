package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Cat.class)
public interface CatEntityVariationSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Holder<CatVariant> variant);

    @Invoker("setCollarColor")
    public void invokeSetCollarColor(DyeColor color);
}
