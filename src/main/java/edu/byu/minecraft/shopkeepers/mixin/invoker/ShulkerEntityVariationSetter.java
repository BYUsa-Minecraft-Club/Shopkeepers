package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(ShulkerEntity.class)
public interface ShulkerEntityVariationSetter {
    @Invoker(value = "setColor")
    public void invokeSetColor(Optional<DyeColor> color);

    @Invoker(value = "getPeekAmount")
    public int invokeGetPeekAmount();

    @Invoker(value = "setPeekAmount")
    public void invokeSetPeekAmount(int peekAmount);

    @Invoker(value = "setAttachedFace")
    public void invokeSetAttachedFace(Direction face);
}
