package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.ShopkeeperInteractions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin extends EntityMixin {

    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    public void injectInteractAt(PlayerEntity player, Vec3d hitPos, Hand hand,
                           CallbackInfoReturnable<ActionResult> cir) {
        ArmorStandEntity self = (ArmorStandEntity) (Object) this;
        if(player instanceof ServerPlayerEntity serverPlayer &&
                ShopkeeperInteractions.openGui(self, serverPlayer)) {
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Inject(method = "handleAttack", at = @At("HEAD"), cancellable = true)
    public void injectHandleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir) {
        ifShopkeeperEntity(() -> cir.setReturnValue(true));
    }

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    public void injectKill(ServerWorld world, CallbackInfo ci) {
        ifShopkeeperEntity(() -> {
            ArmorStandEntity self = (ArmorStandEntity) (Object) this;
            ShopkeeperInteractions.warnDeathPrevented(self);
            ci.cancel();
        });
    }
}
