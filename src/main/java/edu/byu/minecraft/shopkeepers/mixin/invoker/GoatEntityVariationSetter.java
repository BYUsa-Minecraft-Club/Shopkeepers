package edu.byu.minecraft.shopkeepers.mixin.invoker;

import edu.byu.minecraft.shopkeepers.customization.GoatCustomizations;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.GoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GoatEntity.class)
public abstract class GoatEntityVariationSetter implements GoatCustomizations.GoatEditor {
    @Accessor(value = "RIGHT_HORN")
    public abstract TrackedData<Boolean> accessRightHorn();

    @Accessor(value = "LEFT_HORN")
    public abstract TrackedData<Boolean> accessLeftHorn();

    @Override
    @Unique
    public void shopkeepers$setHasHorn(boolean rightHorn, boolean hasHorn) {
        ((EntityDataTrackerAccessor) this).getDataTracker().set(rightHorn ? accessRightHorn() : accessLeftHorn(), hasHorn);
    }
}
