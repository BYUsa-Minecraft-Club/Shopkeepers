package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.ShopkeeperInteractions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin {

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void injectOnDeath(DamageSource damageSource, CallbackInfo ci) {
        ifShopkeeperEntity(() -> {
            LivingEntity self = (LivingEntity) (Object) this;
            self.setHealth(Float.MAX_VALUE);
            ShopkeeperInteractions.warnDeathPrevented(self);
            ci.cancel();
        });
    }
}
