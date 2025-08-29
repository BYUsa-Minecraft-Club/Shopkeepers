package edu.byu.minecraft.shopkeepers.mixin;

import edu.byu.minecraft.Shopkeepers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;onStruckByLightning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LightningEntity;)V"))
    private void redirectOnStruckByLightning(Entity instance, ServerWorld world, LightningEntity lightning) {
        if (!Shopkeepers.getData().getData().containsKey(instance.getUuid())) {
            instance.onStruckByLightning(world, lightning);
        }
    }

}
