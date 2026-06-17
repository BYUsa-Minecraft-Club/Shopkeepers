package edu.byu.minecraft.shopkeepers.mixin.invoker;

import edu.byu.minecraft.shopkeepers.customization.appearance.GoatCustomizations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Goat.class)
public abstract class GoatEntityVariationSetter implements GoatCustomizations.GoatEditor {
    @Accessor(value = "DATA_HAS_RIGHT_HORN")
    public static EntityDataAccessor<Boolean> accessRightHorn() {
        throw new AssertionError();
    }

    @Accessor(value = "DATA_HAS_LEFT_HORN")
    public static EntityDataAccessor<Boolean> accessLeftHorn() {
        throw new AssertionError();
    }

    @Override
    @Unique
    public void shopkeepers$setHasHorn(boolean rightHorn, boolean hasHorn) {
        ((EntityDataTrackerAccessor) this).getDataTracker().set(rightHorn ? accessRightHorn() : accessLeftHorn(), hasHorn);
    }
}
