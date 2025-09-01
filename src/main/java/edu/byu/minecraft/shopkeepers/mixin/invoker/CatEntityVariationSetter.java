package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CatEntity.class)
public interface CatEntityVariationSetter {
    @Invoker(value = "setVariant")
    public void invokeSetVariant(RegistryEntry<CatVariant> variant);

    @Invoker("setCollarColor")
    public void invokeSetCollarColor(DyeColor color);
}
