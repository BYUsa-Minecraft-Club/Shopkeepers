package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Wolf.class)
public interface WolfEntityVariationAccesor {

    @Invoker(value = "getVariant")
    public Holder<WolfVariant> invokeGetVariant();

    @Invoker(value = "setVariant")
    public void invokeSetVariant(Holder<WolfVariant> variant);

    @Invoker("setCollarColor")
    public void invokeSetCollarColor(DyeColor color);
}
