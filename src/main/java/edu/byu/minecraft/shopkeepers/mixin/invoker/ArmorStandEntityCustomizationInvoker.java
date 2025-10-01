package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandEntityCustomizationInvoker {
    @Invoker("setSmall")
    void invokeSetSmall(boolean small);
}
