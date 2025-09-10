package edu.byu.minecraft.shopkeepers.mixin.invoker;

import edu.byu.minecraft.shopkeepers.customization.appearance.CreeperCustomizations;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityVariationSetter implements CreeperCustomizations.CreeperEditor {
    @Accessor(value = "CHARGED")
    public static TrackedData<Boolean> accessCharged() {
        throw new AssertionError();
    }

    @Unique
    public void shopkeepers$setCharged(boolean charged) {
        ((EntityDataTrackerAccessor) this).getDataTracker().set(accessCharged(), charged);
    }

}
