package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TropicalFish.class)
public interface TropicalFishEntityVariationSetter {
    @Invoker(value = "setPatternColor")
    public void invokeSetPatternColor(DyeColor color);

    @Invoker(value = "setBaseColor")
    public void invokeSetBaseColor(DyeColor color);

    @Invoker(value = "setPattern")
    public void invokeSetVariety(TropicalFish.Pattern pattern);
}
