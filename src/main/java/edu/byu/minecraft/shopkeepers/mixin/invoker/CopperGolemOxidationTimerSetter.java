package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.CopperGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CopperGolemEntity.class)
public interface CopperGolemOxidationTimerSetter {
    @Accessor("nextOxidationAge")
    void setNextOxidationAge(long nextOxidationAge);
}
