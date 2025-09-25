package edu.byu.minecraft.shopkeepers.mixin;

import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This is mostly to fix errors with trades when the "Server Translations API" mod is loaded. Hopefully this doesn't
 * cause other errors, but if this mod causes other errors with translations, this is a good place to start looking
 */
@Mixin(TranslatableTextContent.class)
public abstract class TranslateableTextContextMixin {
    @Mutable @Final @Shadow private String fallback;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyFallback(String key, String fallback, Object[] args, CallbackInfo ci) {
        if(fallback != null && fallback.startsWith("item.minecraft")) {
            this.fallback = null;
        }
    }

}
