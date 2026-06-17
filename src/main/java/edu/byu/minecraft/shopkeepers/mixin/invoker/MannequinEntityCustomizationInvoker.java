package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mannequin.class)
public interface MannequinEntityCustomizationInvoker {
    @Invoker("setProfile")
    void invokeSetMannequinProfile(ResolvableProfile pc);

    @Invoker("getProfile")
    ResolvableProfile invokeGetMannequinProfile();

    @Invoker("setImmovable")
    void invokeSetImmovable(boolean immovable);

    @Invoker("setDescription")
    void invokeSetDescription(Component description);
}
