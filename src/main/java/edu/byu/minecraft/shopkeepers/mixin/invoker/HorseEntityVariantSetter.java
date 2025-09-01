package edu.byu.minecraft.shopkeepers.mixin.invoker;


import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HorseEntity.class)
public interface HorseEntityVariantSetter {
    @Invoker(value = "setHorseVariant")
    public void invokeSetVariant(HorseColor color, HorseMarking marking);
}
