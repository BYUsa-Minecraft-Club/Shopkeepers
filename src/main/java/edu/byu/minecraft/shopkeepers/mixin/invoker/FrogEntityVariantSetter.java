package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Frog.class)
public interface FrogEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Holder<FrogVariant> variant);
}
