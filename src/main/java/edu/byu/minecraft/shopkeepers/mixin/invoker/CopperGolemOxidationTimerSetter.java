package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.golem.CopperGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CopperGolem.class)
public interface CopperGolemOxidationTimerSetter {
    @Accessor("nextWeatheringTick")
    void setNextOxidationAge(long nextOxidationAge);
}
