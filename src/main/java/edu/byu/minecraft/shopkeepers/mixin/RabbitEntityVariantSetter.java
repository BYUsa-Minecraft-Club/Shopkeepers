package edu.byu.minecraft.shopkeepers.mixin;

import net.minecraft.entity.passive.RabbitEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RabbitEntity.class)
public interface RabbitEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(RabbitEntity.Variant variant);
}
