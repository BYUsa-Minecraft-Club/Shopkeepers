package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFishEntity.class)
public interface TropicalFishEntityVariationSetter {
    @Invoker(value = "setPatternColor")
    public void invokeSetPatternColor(DyeColor color);

    @Invoker(value = "setBaseColor")
    public void invokeSetBaseColor(DyeColor color);

    @Invoker(value = "setVariety")
    public void invokeSetVariety(TropicalFishEntity.Pattern pattern);
}
