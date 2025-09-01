package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.LlamaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LlamaEntity.class)
public interface LlamaEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(LlamaEntity.Variant variant);
}
