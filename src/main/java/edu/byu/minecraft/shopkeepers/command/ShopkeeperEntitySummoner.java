package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import edu.byu.minecraft.shopkeepers.mixin.invoker.CopperGolemOxidationTimerSetter;
import edu.byu.minecraft.shopkeepers.mixin.invoker.MannequinEntityCustomizationInvoker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.phys.Vec3;

public class ShopkeeperEntitySummoner {

    public static Entity summon(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType,
                                Vec3 pos, boolean onGround, boolean initialize) throws CommandSyntaxException {
        Entity entity = SummonCommand.createEntity(source, entityType, pos, shopkeeperSummonNbt(onGround), initialize);

        if(initialize) {
            if(entity instanceof Mob me) {
                me.setBaby(false);
                me.setLeftHanded(false);
                for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                    me.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
            if (entity instanceof AbstractHorse ahe) {
                ahe.setTamed(true);
            }
            if (entity instanceof Chicken chicken) {
                chicken.eggTime = 2099999999;
            }
            if (entity instanceof CopperGolem copperGolem) {
                ((CopperGolemOxidationTimerSetter) (Object) copperGolem).setNextOxidationAge(-2L);
            }
            if (entity instanceof Mannequin mannequin && source.getPlayer() != null) {
                MannequinEntityCustomizationInvoker asInvoker = (MannequinEntityCustomizationInvoker) mannequin;
                asInvoker.invokeSetMannequinProfile(ResolvableProfile.createUnresolved(source.getPlayer().getUUID()));
                asInvoker.invokeSetImmovable(true);
                asInvoker.invokeSetDescription(Component.nullToEmpty("NPC Shopkeeper"));
            }
        }
        return entity;
    }

    private static CompoundTag shopkeeperSummonNbt(boolean onGround) {
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean("NoAI", true);
        nbt.putBoolean("Invulnerable", true);
        nbt.putBoolean("Silent", true);
        nbt.putBoolean("OnGround", onGround);
        nbt.putBoolean("NoGravity", true); //If you want to remove the no gravity, remove the move prevention from
        // EntityMixin (and you may need to find a new way to do that otherwise shopkeepers will be movable by
        // pistons), otherwise the game will become a lag-fest when any shopkeepers are loaded and not on the ground
        nbt.putBoolean("CanPickUpLoot", false);
        nbt.putBoolean("PersistenceRequired", true);
        nbt.putBoolean("IsImmuneToZombification", true);
        nbt.putBoolean("CannotBeHunted", true);

        return nbt;
    }
}
