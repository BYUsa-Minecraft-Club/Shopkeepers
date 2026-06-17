package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.equine.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Llama.class)
public interface LlamaEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Llama.Variant variant);
}
