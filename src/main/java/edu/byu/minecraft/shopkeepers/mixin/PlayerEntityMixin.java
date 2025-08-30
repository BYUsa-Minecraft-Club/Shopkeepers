package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.gui.AdminShopTradeSetupGui;
import edu.byu.minecraft.shopkeepers.gui.OfferGui;
import edu.byu.minecraft.shopkeepers.gui.PlayerShopTradeSetupGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        //ignore the warning on this line. Because this is a mixin, the compiler says it can never be a
        //ServerPlayerEntity but it will be at runtime (sometimes, hence the line)
        if(!( ((Object) this) instanceof ServerPlayerEntity player)) return;

        //this method sometimes fires twice, one with main hand and one with off hand. I wanted to filter out so the
        // stuff that matters only happens once.
        if (hand == Hand.OFF_HAND) return;

        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(entity.getUuid());
        if (shopkeeperData != null && player.getServer() != null) {
            if(shopkeeperData.isAdmin() && player.getServer().getPlayerManager().isOperator(player.getGameProfile())) {
                if(shopkeeperData.trades().isEmpty()) {
                    new AdminShopTradeSetupGui(player, entity).open();
                } else {
                    new OfferGui(player, entity).open();
                }
            } else if (shopkeeperData.owners().contains(player.getUuid())) {
                new PlayerShopTradeSetupGui(player, entity).open();
            } else {
                new OfferGui(player, entity).open();
            }

            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }
    }

}