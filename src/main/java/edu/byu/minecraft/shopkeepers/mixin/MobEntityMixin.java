package edu.byu.minecraft.shopkeepers.mixin;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin extends EntityMixin {
    @Inject(method = "isAffectedByDaylight", at = @At("HEAD"), cancellable = true)
    private void isAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        ifShopkeeperEntity(() -> {
            cir.setReturnValue(false);
            cir.cancel();
        });
    }
}
