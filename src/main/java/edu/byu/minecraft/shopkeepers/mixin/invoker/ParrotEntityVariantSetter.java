package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.ParrotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ParrotEntity.class)
public interface ParrotEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(ParrotEntity.Variant variant);
}
