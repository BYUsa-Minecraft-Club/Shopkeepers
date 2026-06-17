package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.ShopkeeperInteractions;
import edu.byu.minecraft.shopkeepers.ShopkeeperMover;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void interact(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        //ignore the warning on this line. Because this is a mixin, the compiler says it can never be a
        //ServerPlayerEntity but it will be at runtime (sometimes, hence the line)
        if(!( ((Object) this) instanceof ServerPlayer player)) return;

        ItemStack is = player.getItemInHand(hand);
        if(ShopkeeperMover.moved(is, player)) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
            return;
        }

        //this method sometimes fires twice, one with main hand and one with off hand. I wanted to filter out so the
        // stuff that matters only happens once.
        if (hand == InteractionHand.OFF_HAND) return;

        if (ShopkeeperInteractions.openGui(entity, player)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

}