package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityVariationSetter {
    @Invoker(value = "setHasNectar")
    public void invokeSetHasNectar(boolean hasNectar);

    @Invoker(value = "setHasStung")
    public void invokeSetHasStung(boolean hasStung);
}
