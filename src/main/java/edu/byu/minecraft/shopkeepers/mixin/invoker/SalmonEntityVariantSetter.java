package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.SalmonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SalmonEntity.class)
public interface SalmonEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(SalmonEntity.Variant variant);
}
