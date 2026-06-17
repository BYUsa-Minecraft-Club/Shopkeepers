package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Fox.class)
public interface FoxEntityVariationSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Fox.Variant variant);

    @Invoker("setSleeping")
    public void invokeSetSleeping(boolean sleeping);
}
