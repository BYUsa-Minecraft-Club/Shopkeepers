package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityDataTrackerAccessor {
    @Accessor("entityData")
    public SynchedEntityData getDataTracker();
}
