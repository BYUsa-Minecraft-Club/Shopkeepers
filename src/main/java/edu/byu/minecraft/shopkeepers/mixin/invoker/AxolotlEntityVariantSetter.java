package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AxolotlEntity.class)
public interface AxolotlEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(AxolotlEntity.Variant variant);
}
