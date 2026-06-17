package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.cow.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MushroomCow.class)
public interface MooshroomEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(MushroomCow.Variant variant);
}
