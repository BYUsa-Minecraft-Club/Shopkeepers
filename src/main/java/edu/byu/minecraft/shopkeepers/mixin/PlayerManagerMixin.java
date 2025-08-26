package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.Shopkeepers;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void injectOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player,
                                      ConnectedClientData clientData, CallbackInfo ci) {
        Shopkeepers.getData().checkPlayer(player);
    }

}
