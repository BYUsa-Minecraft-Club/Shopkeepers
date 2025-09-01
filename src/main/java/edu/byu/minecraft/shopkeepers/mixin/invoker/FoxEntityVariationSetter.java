package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.FoxEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoxEntity.class)
public interface FoxEntityVariationSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(FoxEntity.Variant variant);

    @Invoker("setSleeping")
    public void invokeSetSleeping(boolean sleeping);
}
