package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {

    //actual tab characters (\t) don't display correctly
    private static final String TAB = "    ";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("shopkeepers")
            .requires(Permissions.require("shopkeepers.admin", 4).and(((Predicate<ServerCommandSource>) ServerCommandSource::isExecutedByPlayer)))
            .executes(Commands::documentation)
                .then(literal("help").executes(Commands::documentation))
                .then(literal("make")
                    .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                            .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                            .executes(Commands::makeNormal)))
                .then(literal("shopentities").executes(Commands::listShopEntities))
                .then(literal("admin").requires(Permissions.require("shopkeepers.admin", 4))
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
                    .then(literal("playershoplimit").executes(Commands::playerShopLimitGet)
                        .then(literal("get").executes(Commands::playerShopLimitGet)
                                .then(argument("player", StringArgumentType.word())
                                        .suggests(CustomSuggestionProviders::allPlayers)
                                        .executes(Commands::onePlayerShopLimitGet)))
                        .then(literal("set").then(CommandManager.argument("max", IntegerArgumentType.integer())
                                .executes(Commands::playerShopLimitSet))
                                    .then(argument("player", StringArgumentType.word())
                                            .suggests(CustomSuggestionProviders::allPlayers)
                                                .then(CommandManager.argument("max", IntegerArgumentType.integer())
                                                        .executes(Commands::onePlayerShopLimitSet))))
                        .then(literal("list").executes(Commands::listShopLimits))
                        .then(literal("remove").then(argument("player", StringArgumentType.word())
                                .suggests(CustomSuggestionProviders::allPlayers)
                                .executes(Commands::onePlayerShopLimitRemove))))
                ));
    }



    private static int documentation(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        StringBuilder builder = new StringBuilder("Shopkeepers usage:\n");

        builder.append(TAB).append("/shopkeepers make <approved entity type>   - Creates a new shopkeeper, owned by you\n");
        builder.append(TAB).append("/shopkeepers shopentities   - lists out the entity types approved by admins\n");

        if(Permissions.check(context.getSource(), "shopkeepers.admin", 4)) {
            builder.append(TAB).append("/shopkeepers admin make <approved entity type>   - Creates a new admin shop\n");
            builder.append(TAB).append("/shopkeepers admin shopentities list   - lists out the approved entity types\n");
            builder.append(TAB).append("/shopkeepers admin shopentities add <summonable entity type>   - Adds entity type to approved list\n");
            builder.append(TAB).append("/shopkeepers admin shopentities remove <summonable entity type>   - Removes entity type from approved list. Does not disband existing shops with removed type\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit get   - displays the current default maximum number of shops players can own\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit get <player name>   - displays the current maximum number of shops for specified player\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit set <integer>   - sets the maximum number of shops players can own\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit set <integer> <player name>   - sets the maximum number of shops specified player can own\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit remove <player name>   - removes the specific limit for specified player (reverts to default)\n");
            builder.append(TAB).append("/shopkeepers admin playershoplimit list   - lists shop limits for any player with a limit set\n");
            builder.append("(click for more readable documentation)");
        } else {
            builder.deleteCharAt(builder.length() - 1);
        }

        Text text = Text.of(builder.toString());
        try {
                text = text.getWithStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(new URI(
                        "https://github.com/BYUsa-Minecraft-Club/Shopkeepers/blob/master/README.md"
                )))).getFirst();
        } catch (URISyntaxException ignored) {}

        context.getSource().sendMessage(text);
        return 1;
    }

    private static int listShopLimits(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        StringBuilder builder = new StringBuilder(String.format("Player shop limits: \n  Default Limit: %s\n",
                Shopkeepers.getData().getMaxOwnedShops()));
        List<String> playersWithLimits = new ArrayList<>();
        for(var entry : Shopkeepers.getData().getPlayerMaxOwnedShops().entrySet()) {
            playersWithLimits.add(String.format("%s%s: %d\n",
                    TAB, Shopkeepers.getData().getPlayers().get(entry.getKey()), entry.getValue()));
        }
        playersWithLimits.sort(Comparator.naturalOrder());
        playersWithLimits.forEach(builder::append);
        builder.deleteCharAt(builder.length() - 1);
        context.getSource().sendMessage(Text.of(builder.toString()));
        return 1;
    }

    private static int onePlayerShopLimitRemove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerId = getPlayerId(playerName);
        Integer oldMax = Shopkeepers.getData().getPlayerMaxOwnedShops().remove(playerId);
        if(oldMax == null) {
            context.getSource().sendMessage(Text.of(String.format("Player %s did not already have a limit", playerName)));
            return 0;
        } else {
            context.getSource().sendMessage(Text.of(String.format("Removed limit from %s (was %d)", playerName, oldMax)));
            return 1;
        }
    }

    private static int onePlayerShopLimitSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        int max = IntegerArgumentType.getInteger(context, "max");
        UUID playerId = getPlayerId(playerName);
        Shopkeepers.getData().getPlayerMaxOwnedShops().put(playerId, max);
        context.getSource().sendMessage(Text.of(String.format("Set new limit for %s of %d", playerName, max)));
        return 1;
    }

    private static int onePlayerShopLimitGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerId = getPlayerId(playerName);
        Integer max = Shopkeepers.getData().getPlayerMaxOwnedShops().get(playerId);
        if(max == null) {
            context.getSource().sendMessage(Text.of(String.format("No specific limit set for %s, default is %d",
                    playerName, Shopkeepers.getData().getMaxOwnedShops())));
        } else {
            context.getSource().sendMessage(Text.of(String.format("Limit for %s is %d", playerName, max)));
        }
        return 1;
    }

    private static UUID getPlayerId(String playerName) throws CommandSyntaxException {
        for(Map.Entry<UUID, String> entry : Shopkeepers.getData().getPlayers().entrySet()) {
            if(entry.getValue().equals(playerName)) return entry.getKey();
        }

        Message errorMessage = () -> "Could not find player with name " + playerName;
        throw new CommandSyntaxException(new SimpleCommandExceptionType(errorMessage), errorMessage);
    }

    private static int playerShopLimitGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendMessage(Text.of("The current limit is " + Shopkeepers.getData().getMaxOwnedShops()));
        return 1;
    }

    private static int playerShopLimitSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int newMax = IntegerArgumentType.getInteger(context, "max");
        Shopkeepers.getData().setMaxOwnedShops(newMax);
        context.getSource().sendMessage(Text.of("New limit set to " + Shopkeepers.getData().getMaxOwnedShops()));
        return 1;
    }

    private static int removeShopEntity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        RegistryEntry.Reference<EntityType<?>>
                entity = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
        if(entity == null) {
            context.getSource().sendMessage(Text.of("That doesn't seem to be a valid entity type"));
            return 0;
        }

        if(Shopkeepers.getData().getAllowedShopkeepers().contains(entity.value())) {
            Shopkeepers.getData().getAllowedShopkeepers().remove(entity.value());
            Shopkeepers.getData().markDirty();
            context.getSource().sendMessage(Text.of(entity.value().getName().getString() + " removed"));
            return 1;
        } else {
            context.getSource().sendMessage(Text.of(entity.value().getName().getString() + " wasn't already approved, doesn't need to be removed"));
            return 0;
        }
    }

    private static int addShopEntity(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if(executor == null) {
            context.getSource().sendMessage(Text.of("this command must be executed by a player"));
            return 0;
        }
        try {
            RegistryEntry.Reference<EntityType<?>>
                    entity = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
            if(entity == null) {
                context.getSource().sendMessage(Text.of("That doesn't seem to be a valid entity type"));
                return 0;
            }

            Entity testEntity = SummonCommand.summon(context.getSource(), entity, executor.getPos().subtract(0, 1000, 0),
                    shopkeeperSummonNbt(false), true);
            if(!(testEntity instanceof MobEntity)) {
                context.getSource().sendMessage(Text.of(entity.value().getName().getString() + " is not a mob, not adding"));
                testEntity.kill(executor.getWorld());
                return 0;
            }
            else {
                Shopkeepers.getData().getAllowedShopkeepers().add(entity.value());
                Shopkeepers.getData().markDirty();
                context.getSource().sendMessage(Text.of(entity.value().getName().getString() + " added"));
                testEntity.kill(executor.getWorld());
                return 1;
            }
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error adding shopkeeper entity", e);
            return 0;
        }
    }

    private static int listShopEntities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        StringBuilder sb = new StringBuilder("Approved Shopkeepers:\n");
        List<EntityType<?>> approvedEntities = new ArrayList<>(Shopkeepers.getData().getAllowedShopkeepers());
        if(approvedEntities.isEmpty()) {
            sb.append("None yet");
        } else {
            approvedEntities.sort(Comparator.comparing(o -> o.getName().getString()));
            for (var entityType : approvedEntities) {
                sb.append(TAB).append(entityType.getName().getString()).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        context.getSource().sendMessage(Text.of(sb.toString()));
        return 1;
    }


    private static int makeNormal(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if (executor == null) {
            context.getSource().sendMessage(Text.of("this command must be executed by a player"));
            return 0;
        }
        double ownedShops = Shopkeepers.getData().getShopkeeperData().values().stream()
                .filter(sd -> sd.owners().contains(executor.getUuid()))
                .mapToDouble(sd -> 1.0 / sd.owners().size())
                .sum();
        int maxShops = Shopkeepers.getData().getMaxOwnedShops();;
        String maxShopOwner = "the";
        if(Shopkeepers.getData().getPlayerMaxOwnedShops().containsKey(executor.getUuid())) {
            maxShops = Shopkeepers.getData().getPlayerMaxOwnedShops().get(executor.getUuid());
            maxShopOwner = "your";
        }
        if (ownedShops >= maxShops) {
            String relationshipDescription = (ownedShops == maxShops) ? "at" : "greater than";
            if(executor.getServer() != null && executor.getServer().getPlayerManager().isOperator(executor.getGameProfile())) {
                executor.sendMessage(Text.of(String.format("Warning: you already own %.2f current shops (adjusted for multiple owners)," +
                        " %s %s normal max of %d. Since you are a server operator," +
                        " you are allowed to bypass the limit. Continuing with shop creation.",
                        ownedShops, relationshipDescription, maxShopOwner, maxShops)));
            } else {
                executor.sendMessage(Text.of(String.format("You already own %.2f current shops (adjusted for multiple owners)," +
                                " %s %s max of %d. Disband another shop(s) if you want to make a new one",
                        ownedShops, relationshipDescription, maxShopOwner, maxShops)));
                return 0;
            }
        }
        return make(context, false);
    }

    private static int makeAdmin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return make(context, true);
    }

    private static int make(CommandContext<ServerCommandSource> context, boolean isAdmin) throws CommandSyntaxException {
        ServerPlayerEntity executor = context.getSource().getPlayer();
        if (executor == null) {
            context.getSource().sendMessage(Text.of("this command must be executed by a player"));
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
                context.getSource().sendMessage(Text.of("That's not an approved shopkeeper entity"));
                return 0;
            }

            Entity shopkeeper = SummonCommand.summon(context.getSource(), entity, executor.getPos(),
                    shopkeeperSummonNbt(executor.isOnGround()), true);
            shopkeeper.teleport(executor.getWorld(), executor.getX(), executor.getY(), executor.getZ(),
                    new HashSet<>(), executor.getYaw(), executor.getPitch(), true);

            List<UUID> owners = new ArrayList<>();
            if(!isAdmin) {
                owners.add(executor.getUuid());
            }

            Shopkeepers.getData().getShopkeeperData().put(shopkeeper.getUuid(), new ShopkeeperData(new ArrayList<>(),
                    new ArrayList<>(), isAdmin, owners));
            Shopkeepers.getData().markDirty();
            return 1;
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error making shopkeeper", e);
            return 0;
        }

    }

    public static NbtCompound shopkeeperSummonNbt(boolean onGround) {
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
