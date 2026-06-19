package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.gui.OfferGui;
import eu.pb4.sgui.impl.virtual.merchant.VirtualMerchantScreenHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientSideMerchant.class)
public class SimpleMerchantMixin {

    @Inject(method = "notifyTrade", at = @At("TAIL"))
    public void injectTrade(MerchantOffer offer, CallbackInfo ci) {
        Player player = ((ClientSideMerchant) (Object) this).getTradingPlayer();
        if (player instanceof ServerPlayer &&
                player.containerMenu instanceof VirtualMerchantScreenHandler vmsh &&
                vmsh.getGui() instanceof OfferGui og) {
            og.afterTrade(offer);
        }
    }
}
