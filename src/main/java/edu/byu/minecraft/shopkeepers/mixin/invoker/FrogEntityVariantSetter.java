package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FrogEntity.class)
public interface FrogEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(RegistryEntry<FrogVariant> variant);
}
