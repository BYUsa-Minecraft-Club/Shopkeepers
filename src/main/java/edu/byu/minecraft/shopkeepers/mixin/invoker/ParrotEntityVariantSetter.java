package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.parrot.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Parrot.class)
public interface ParrotEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Parrot.Variant variant);
}
