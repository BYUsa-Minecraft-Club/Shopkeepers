package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.rabbit.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Rabbit.class)
public interface RabbitEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Rabbit.Variant variant);
}
