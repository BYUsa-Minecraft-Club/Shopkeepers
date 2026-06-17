package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.ShopkeeperInteractions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public class ArmorStandEntityMixin extends EntityMixin {

    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    public void injectInteractAt(Player player, Vec3 hitPos, InteractionHand hand,
                           CallbackInfoReturnable<InteractionResult> cir) {
        ArmorStand self = (ArmorStand) (Object) this;
        if(player instanceof ServerPlayer serverPlayer &&
                ShopkeeperInteractions.openGui(self, serverPlayer)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "skipAttackInteraction", at = @At("HEAD"), cancellable = true)
    public void injectHandleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir) {
        ifShopkeeperEntity(() -> cir.setReturnValue(true));
    }

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    public void injectKill(ServerLevel world, CallbackInfo ci) {
        ifShopkeeperEntity(() -> {
            ArmorStand self = (ArmorStand) (Object) this;
            ShopkeeperInteractions.warnDeathPrevented(self);
            ci.cancel();
        });
    }
}
