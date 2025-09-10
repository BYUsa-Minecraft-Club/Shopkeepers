package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.util.math.Vec3d;

public class ShopkeeperEntitySummoner {

    public static Entity summon(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType,
                                Vec3d pos, boolean onGround, boolean initialize) throws CommandSyntaxException {
        Entity entity = SummonCommand.summon(source, entityType, pos, shopkeeperSummonNbt(onGround), initialize);

        if(initialize) {
            if(entity instanceof MobEntity me) {
                me.setBaby(false);
                for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                    me.equipStack(equipmentSlot, ItemStack.EMPTY);
                }
            }
            if (entity instanceof AbstractHorseEntity ahe) {
                ahe.setTame(true);
            }
        }
        return entity;
    }

    private static NbtCompound shopkeeperSummonNbt(boolean onGround) {
        NbtCompound nbt = new NbtCompound();

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
