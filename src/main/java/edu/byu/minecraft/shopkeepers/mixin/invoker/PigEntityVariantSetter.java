package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Pig.class)
public interface PigEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Holder<PigVariant> variant);
}
