package edu.byu.minecraft.shopkeepers.mixin.invoker;

import edu.byu.minecraft.shopkeepers.customization.appearance.CreeperCustomizations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public abstract class CreeperEntityVariationSetter implements CreeperCustomizations.CreeperEditor {
    @Accessor(value = "DATA_IS_POWERED")
    public static EntityDataAccessor<Boolean> accessCharged() {
        throw new AssertionError();
    }

    @Unique
    public void shopkeepers$setCharged(boolean charged) {
        ((EntityDataTrackerAccessor) this).getDataTracker().set(accessCharged(), charged);
    }

}
