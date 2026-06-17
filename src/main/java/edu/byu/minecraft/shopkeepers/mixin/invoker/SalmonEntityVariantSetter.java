package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.fish.Salmon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Salmon.class)
public interface SalmonEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Salmon.Variant variant);
}
