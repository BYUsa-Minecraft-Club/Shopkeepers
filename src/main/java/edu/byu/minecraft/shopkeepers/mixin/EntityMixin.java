package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.Shopkeepers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    //Cannot put shopkeepers into minecarts or boats
    @Inject(method = "canStartRiding", at = @At("HEAD"), cancellable = true)
    private void canStartRiding(CallbackInfoReturnable<Boolean> cir) {
        ifShopkeeperEntity(() -> {
            cir.setReturnValue(false);
            cir.cancel();
        });
    }

    //Make all shopkeepers immune to fire (mostly for uncovered zombies in daylight)
    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        ifShopkeeperEntity(() -> {
            cir.setReturnValue(true);
            cir.cancel();
        });
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d movement, CallbackInfo ci) {
        ifShopkeeperEntity(() -> {
            ci.cancel();
        });
    }

    @Unique
    private void ifShopkeeperEntity(Runnable action) {
        Entity self = (Entity) (Object) this;
        if(Shopkeepers.getData().getData().containsKey(self.getUuid())) {
            action.run();
        }
    }

}
