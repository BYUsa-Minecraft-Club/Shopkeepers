package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.shopkeepers.gui.OfferGui;
import eu.pb4.sgui.virtual.merchant.VirtualMerchantScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.SimpleMerchant;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleMerchant.class)
public class SimpleMerchantMixin {

    @Inject(method = "trade", at = @At("TAIL"))
    public void injectTrade(TradeOffer offer, CallbackInfo ci) {
        PlayerEntity player = ((SimpleMerchant) (Object) this).getCustomer();
        if (player instanceof ServerPlayerEntity &&
                player.currentScreenHandler instanceof VirtualMerchantScreenHandler vmsh &&
                vmsh.getGui() instanceof OfferGui og) {
            og.afterTrade(offer);
        }
    }
}
