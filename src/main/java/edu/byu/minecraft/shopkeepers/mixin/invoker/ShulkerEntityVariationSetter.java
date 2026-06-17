package edu.byu.minecraft.shopkeepers.mixin.invoker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;

@Mixin(Shulker.class)
public interface ShulkerEntityVariationSetter {
    @Invoker(value = "setVariant")
    public void invokeSetColor(Optional<DyeColor> color);

    @Invoker(value = "getRawPeekAmount")
    public int invokeGetPeekAmount();

    @Invoker(value = "setRawPeekAmount")
    public void invokeSetPeekAmount(int peekAmount);

    @Invoker(value = "setAttachFace")
    public void invokeSetAttachedFace(Direction face);
}
