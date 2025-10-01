package edu.byu.minecraft.shopkeepers.mixin.invoker;

import edu.byu.minecraft.shopkeepers.customization.appearance.CopperGolemCustomizations.CopperGolemOxidationTimerSetter;
import net.minecraft.entity.passive.CopperGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CopperGolemEntity.class)
public abstract class CopperGolemEntityMixin implements CopperGolemOxidationTimerSetter {
    @Accessor("nextOxidationAge")
    public abstract void setNextOxidationAge(long nextOxidationAge);
}
