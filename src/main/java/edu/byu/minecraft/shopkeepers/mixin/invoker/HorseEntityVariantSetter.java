package edu.byu.minecraft.shopkeepers.mixin.invoker;


import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Markings;
import net.minecraft.world.entity.animal.equine.Variant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Horse.class)
public interface HorseEntityVariantSetter {
    @Invoker(value = "setVariantAndMarkings")
    public void invokeSetVariant(Variant color, Markings marking);
}
