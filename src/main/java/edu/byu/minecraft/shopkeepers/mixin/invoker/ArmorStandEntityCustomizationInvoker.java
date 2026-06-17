package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStand.class)
public interface ArmorStandEntityCustomizationInvoker {
    @Invoker("setSmall")
    void invokeSetSmall(boolean small);
}
