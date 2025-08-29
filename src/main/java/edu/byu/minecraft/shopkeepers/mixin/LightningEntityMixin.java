package edu.byu.minecraft.shopkeepers.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void filterLightningTargets(CallbackInfo ci, @Local List<Entity> list) {
        list.removeIf(entity -> Shopkeepers.getData().getData().containsKey(entity.getUuid()));
    }

}
