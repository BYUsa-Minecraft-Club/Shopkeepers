package edu.byu.minecraft.shopkeepers.mixin;

import net.minecraft.entity.passive.TurtleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEntity.class)
public class TurtleEntityMixin extends EntityMixin {
    @Inject(method = "onGrowUp", at = @At("HEAD"), cancellable = true)
    private void onGrowUp(CallbackInfo ci) {
        ifShopkeeperEntity(ci::cancel);
    }
}
