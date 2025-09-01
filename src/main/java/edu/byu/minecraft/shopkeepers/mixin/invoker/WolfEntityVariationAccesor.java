package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.WolfVariant;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WolfEntity.class)
public interface WolfEntityVariationAccesor {

    @Invoker(value = "getVariant")
    public RegistryEntry<WolfVariant> invokeGetVariant();

    @Invoker(value = "setVariant")
    public void invokeSetVariant(RegistryEntry<WolfVariant> variant);

    @Invoker("setCollarColor")
    public void invokeSetCollarColor(DyeColor color);
}
