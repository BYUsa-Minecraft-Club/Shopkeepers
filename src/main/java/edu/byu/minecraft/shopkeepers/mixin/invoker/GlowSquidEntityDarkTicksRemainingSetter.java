package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.GlowSquidEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GlowSquidEntity.class)
public interface GlowSquidEntityDarkTicksRemainingSetter {
    @Invoker(value = "setDarkTicksRemaining")
    public void invokeSetDarkTicksRemaining(int ticks);
}
