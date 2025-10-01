package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MannequinEntity.class)
public interface MannequinEntityCustomizationInvoker {
    @Invoker("setMannequinProfile")
    void invokeSetMannequinProfile(ProfileComponent pc);

    @Invoker("setImmovable")
    void invokeSetImmovable(boolean immovable);

    @Invoker("setDescription")
    void invokeSetDescription(Text description);
}
