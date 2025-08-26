package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.server.command.CommandManager.literal;

public class Commands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("shopkeepers").requires(((Predicate<ServerCommandSource>) ServerCommandSource::isExecutedByPlayer))
                .then(literal("make")
                        .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                                .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                                .executes(Commands::makeNormal)))
                .then(literal("admin").requires(Permissions.require("shopkeepers.admin", 3))
                        .then(literal("shopentities")
                            .then(literal("list").executes(Commands::listShopEntities))
                            .then(literal("add")
                                .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                                    .suggests(SuggestionProviders.byId(Identifier.ofVanilla("summonable_entities")))
                                                .executes(Commands::addShopEntity)))
                            .then(literal("remove")
                                .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                                        .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                                        .executes(Commands::removeShopEntity))))
                        .then(literal("make")
                            .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                                .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                                .executes(Commands::makeAdmin)))
                ));
    }

    private static int removeShopEntity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if(executor == null) {
            Shopkeepers.LOGGER.error("removeShopEntity: executor is null");
            return 0;
        }
        RegistryEntry.Reference<EntityType<?>>
                entity = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
        if(entity == null) {
            executor.sendMessage(Text.of("That doesn't seem to be a valid entity type"));
            return 0;
        }

        if(Shopkeepers.getData().getAllowedShopkeepers().contains(entity.value())) {
            Shopkeepers.getData().getAllowedShopkeepers().remove(entity.value());
            Shopkeepers.getData().markDirty();
            executor.sendMessage(Text.of(entity.value().getName().getString() + " removed"));
            return 1;
        } else {
            executor.sendMessage(Text.of(entity.value().getName().getString() + " wasn't already approved, doesn't need to be removed"));
            return 0;
        }
    }

    private static int addShopEntity(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if(executor == null) {
            Shopkeepers.LOGGER.error("addShopEntity: executor is null");
            return 0;
        }
        try {
            RegistryEntry.Reference<EntityType<?>>
                    entity = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
            if(entity == null) {
                executor.sendMessage(Text.of("That doesn't seem to be a valid entity type"));
                return 0;
            }

            Entity testEntity = SummonCommand.summon(context.getSource(), entity, executor.getPos().add(0, 1000, 0),
                    shopkeeperSummonNbt(), true);
            if(!(testEntity instanceof MobEntity)) {
                executor.sendMessage(Text.of(entity.value().getName().getString() + " is not a mob, not adding"));
                testEntity.kill(executor.getWorld());
                return 0;
            }
            else {
                Shopkeepers.getData().getAllowedShopkeepers().add(entity.value());
                Shopkeepers.getData().markDirty();
                executor.sendMessage(Text.of(entity.value().getName().getString() + " added"));
                testEntity.kill(executor.getWorld());
                return 1;
            }
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error adding shopkeeper entity", e);
            return 0;
        }
    }

    private static int listShopEntities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if (executor == null) {
            Shopkeepers.LOGGER.error("listShopEntities: executor is null");
            return 0;
        }
        StringBuilder sb = new StringBuilder("Approved Shopkeepers:\n");
        List<EntityType<?>> approvedEntities = new ArrayList<>(Shopkeepers.getData().getAllowedShopkeepers());
        if(approvedEntities.isEmpty()) {
            sb.append("None yet");
        } else {
            approvedEntities.sort(Comparator.comparing(o -> o.getName().getString()));
            for (var entityType : approvedEntities) {
                sb.append("    ").append(entityType.getName().getString()).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        executor.sendMessage(Text.of(sb.toString()));
        return 1;
    }


    private static int makeNormal(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return make(context, false);
    }

    private static int makeAdmin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return make(context, true);
    }

    private static int make(CommandContext<ServerCommandSource> context, boolean isAdmin) throws CommandSyntaxException {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if (executor == null) {
            Shopkeepers.LOGGER.error("make shopkeeper: executor is null");
            return 0;
        }
        try {
            RegistryEntry.Reference<EntityType<?>>
                    entity = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
            if(entity == null) {
                Identifier villagerId = Registries.ENTITY_TYPE.getId(EntityType.VILLAGER);
                entity = Registries.ENTITY_TYPE.get(villagerId).getRegistryEntry();
            }

            if(!Shopkeepers.getData().getAllowedShopkeepers().contains(entity.value())) {
                executor.sendMessage(Text.of("That's not an approved shopkeeper entity"));
                return 0;
            }

            Entity shopkeeper = SummonCommand.summon(context.getSource(), entity, executor.getPos(), shopkeeperSummonNbt(), true);
            shopkeeper.teleport(executor.getWorld(), executor.getX(), executor.getY(), executor.getZ(),
                    new HashSet<>(), executor.getYaw(), executor.getPitch(), true);

            List<UUID> owners = new ArrayList<>();
            if(!isAdmin) {
                owners.add(executor.getUuid());
            }

            Shopkeepers.getData().getData().put(shopkeeper.getUuid(), new ShopkeeperData(new ArrayList<>(),
                    new ArrayList<>(), isAdmin, owners));
            Shopkeepers.getData().markDirty();
            return 1;
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error making shopkeeper", e);
            return 0;
        }

    }

    private static NbtCompound shopkeeperSummonNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putBoolean("NoAI", true);
        nbt.putBoolean("Invulnerable", true);
        nbt.putBoolean("Silent", true);
        nbt.putBoolean("CanPickUpLoot", false);
        nbt.putBoolean("PersistenceRequired", true);
        nbt.putBoolean("IsImmuneToZombification", true);
        nbt.putBoolean("CannotBeHunted", true);

        return nbt;
    }

}
