package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.squid.GlowSquid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GlowSquid.class)
public interface GlowSquidEntityDarkTicksRemainingSetter {
    @Invoker(value = "setDarkTicks")
    public void invokeSetDarkTicksRemaining(int ticks);
}
