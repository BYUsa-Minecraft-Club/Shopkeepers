package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PigVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PigEntity.class)
public interface PigEntityVariantSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(RegistryEntry<PigVariant> variant);
}
