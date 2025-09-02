package edu.byu.minecraft.shopkeepers.mixin.invoker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityDataTrackerAccessor {
    @Accessor("dataTracker")
    public DataTracker getDataTracker();
}
