package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.Shopkeepers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.UUID;
import java.util.function.Function;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void injectOnDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (Shopkeepers.getData().getShopkeeperData().containsKey(self.getUuid())) {
            self.setHealth(Float.MAX_VALUE);
            String warning = String.format("Shopkeeper entity %s owned by %s was attempted to be killed (likely by a command). " +
                            "Please disband the shop first. Location: X: %.0f, Y:%.0f, Z:%.0f",
                    self.getName().getString(),
                    Shopkeepers.getData().getShopkeeperData().get(self.getUuid()).owners().stream().map(
                            uuid -> Shopkeepers.getData().getPlayers().get(uuid)).sorted(String::compareToIgnoreCase).toList(),
                    self.getPos().getX(), self.getPos().getY(), self.getPos().getZ());

            if(self.getServer() != null) {
                for (String opName : self.getServer().getPlayerManager().getOpNames()) {
                    ServerPlayerEntity op = self.getServer().getPlayerManager().getPlayer(opName);
                    if (op != null) {
                        op.sendMessage(Text.of(warning));
                    }
                }
            }

            Shopkeepers.LOGGER.warn(warning);
            ci.cancel();
        }
    }
}
