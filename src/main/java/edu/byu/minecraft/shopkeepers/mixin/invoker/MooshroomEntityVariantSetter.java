package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.MooshroomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MooshroomEntity.class)
public interface MooshroomEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(MooshroomEntity.Variant variant);
}
