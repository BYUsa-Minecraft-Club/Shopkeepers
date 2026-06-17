package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Axolotl.class)
public interface AxolotlEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(Axolotl.Variant variant);
}
