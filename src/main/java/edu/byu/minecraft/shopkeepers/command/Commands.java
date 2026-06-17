package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Commands {

    //actual tab characters (\t) don't display correctly
    private static final String TAB = "    ";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess,
                                net.minecraft.commands.Commands.CommandSelection environment) {
        dispatcher.register(literal("shopkeepers")
            .requires((Predicate<CommandSourceStack>) CommandSourceStack::isPlayer)
            .executes(Commands::documentation)
                .then(literal("help").executes(Commands::documentation))
                .then(literal("make")
                    .then(net.minecraft.commands.Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE))
                            .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                            .executes(Commands::makeNormal)))
                .then(literal("shopentities").executes(Commands::listShopEntities))
                .then(literal("admin").requires(Permissions.require("shopkeepers.admin", 4))
                    .then(literal("shopentities")
                        .then(literal("list").executes(Commands::listShopEntities))
                        .then(literal("add")
                            .then(net.minecraft.commands.Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE))
                                    .suggests(SuggestionProviders.getProvider(Identifier.withDefaultNamespace("summonable_entities")))
                                    .suggests(CustomSuggestionProviders::unaddedEntityTypes)
                                    .executes(Commands::addShopEntity)))
                        .then(literal("remove")
                            .then(net.minecraft.commands.Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE))
                                    .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                                    .executes(Commands::removeShopEntity))))
                    .then(literal("make")
                        .then(net.minecraft.commands.Commands.argument("entity", ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE))
                                .suggests(CustomSuggestionProviders::approvedShopkeeperEntities)
                                .executes(Commands::makeAdmin)))
                    .then(literal("playershoplimit").executes(Commands::playerShopLimitGet)
                        .then(literal("get").executes(Commands::playerShopLimitGet)
                                .then(argument("player", StringArgumentType.word())
                                        .suggests(CustomSuggestionProviders::allPlayers)
                                        .executes(Commands::onePlayerShopLimitGet)))
                        .then(literal("set").then(net.minecraft.commands.Commands.argument("max", IntegerArgumentType.integer())
                                .executes(Commands::playerShopLimitSet))
                                    .then(argument("player", StringArgumentType.word())
                                            .suggests(CustomSuggestionProviders::allPlayers)
                                                .then(net.minecraft.commands.Commands.argument("max", IntegerArgumentType.integer())
                                                        .executes(Commands::onePlayerShopLimitSet))))
                        .then(literal("list").executes(Commands::listShopLimits))
                        .then(literal("remove").then(argument("player", StringArgumentType.word())
                                .suggests(CustomSuggestionProviders::allPlayers)
                                .executes(Commands::onePlayerShopLimitRemove))))
                ));
    }



    private static int documentation(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
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

        Component text = Component.nullToEmpty(builder.toString());
        try {
                text = text.toFlatList(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(new URI(
                        "https://github.com/BYUsa-Minecraft-Club/Shopkeepers/blob/master/README.md"
                )))).getFirst();
        } catch (URISyntaxException ignored) {}

        context.getSource().sendSystemMessage(text);
        return 1;
    }

    private static int listShopLimits(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
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
        context.getSource().sendSystemMessage(Component.nullToEmpty(builder.toString()));
        return 1;
    }

    private static int onePlayerShopLimitRemove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerId = getPlayerId(playerName);
        Integer oldMax = Shopkeepers.getData().getPlayerMaxOwnedShops().remove(playerId);
        if(oldMax == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty(String.format("Player %s did not already have a limit", playerName)));
            return 0;
        } else {
            context.getSource().sendSystemMessage(Component.nullToEmpty(String.format("Removed limit from %s (was %d)", playerName, oldMax)));
            return 1;
        }
    }

    private static int onePlayerShopLimitSet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        int max = IntegerArgumentType.getInteger(context, "max");
        UUID playerId = getPlayerId(playerName);
        Shopkeepers.getData().getPlayerMaxOwnedShops().put(playerId, max);
        context.getSource().sendSystemMessage(Component.nullToEmpty(String.format("Set new limit for %s of %d", playerName, max)));
        return 1;
    }

    private static int onePlayerShopLimitGet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerId = getPlayerId(playerName);
        Integer max = Shopkeepers.getData().getPlayerMaxOwnedShops().get(playerId);
        if(max == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty(String.format("No specific limit set for %s, default is %d",
                    playerName, Shopkeepers.getData().getMaxOwnedShops())));
        } else {
            context.getSource().sendSystemMessage(Component.nullToEmpty(String.format("Limit for %s is %d", playerName, max)));
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

    private static int playerShopLimitGet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSystemMessage(Component.nullToEmpty("The current limit is " + Shopkeepers.getData().getMaxOwnedShops()));
        return 1;
    }

    private static int playerShopLimitSet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int newMax = IntegerArgumentType.getInteger(context, "max");
        Shopkeepers.getData().setMaxOwnedShops(newMax);
        context.getSource().sendSystemMessage(Component.nullToEmpty("New limit set to " + Shopkeepers.getData().getMaxOwnedShops()));
        return 1;
    }

    private static int removeShopEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Holder.Reference<EntityType<?>>
                entity = ResourceArgument.getSummonableEntityType(context, "entity");
        if(entity == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("That doesn't seem to be a valid entity type"));
            return 0;
        }

        if(Shopkeepers.getData().getAllowedShopkeepers().contains(entity.value())) {
            Shopkeepers.getData().getAllowedShopkeepers().remove(entity.value());
            Shopkeepers.getData().setDirty();
            context.getSource().sendSystemMessage(Component.nullToEmpty(entity.value().getDescription().getString() + " removed"));
            return 1;
        } else {
            context.getSource().sendSystemMessage(Component.nullToEmpty(entity.value().getDescription().getString() + " wasn't already approved, doesn't need to be removed"));
            return 0;
        }
    }

    private static int addShopEntity(CommandContext<CommandSourceStack> context) {
        ServerPlayer executor = context.getSource().getPlayer();
        if(executor == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("this command must be executed by a player"));
            return 0;
        }
        try {
            Holder.Reference<EntityType<?>>
                    entity = ResourceArgument.getSummonableEntityType(context, "entity");
            if(entity == null) {
                context.getSource().sendSystemMessage(Component.nullToEmpty("That doesn't seem to be a valid entity type"));
                return 0;
            }

            Entity testEntity = ShopkeeperEntitySummoner.summon(context.getSource(), entity,
                    new Vec3(executor.getX(), -100, executor.getZ()), false, false);
            if(!(testEntity instanceof LivingEntity)) {
                context.getSource().sendSystemMessage(Component.nullToEmpty(entity.value().getDescription().getString() +
                        " is not a living entity, not adding"));
                testEntity.kill(executor.level());
                return 0;
            }
            else {
                Shopkeepers.getData().getAllowedShopkeepers().add(entity.value());
                Shopkeepers.getData().setDirty();
                context.getSource().sendSystemMessage(Component.nullToEmpty(entity.value().getDescription().getString() + " added"));
                testEntity.kill(executor.level());
                return 1;
            }
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error adding shopkeeper entity", e);
            return 0;
        }
    }

    private static int listShopEntities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        StringBuilder sb = new StringBuilder("Approved Shopkeepers:\n");
        List<EntityType<?>> approvedEntities = new ArrayList<>(Shopkeepers.getData().getAllowedShopkeepers());
        if(approvedEntities.isEmpty()) {
            sb.append("None yet");
        } else {
            approvedEntities.sort(Comparator.comparing(o -> o.getDescription().getString()));
            for (var entityType : approvedEntities) {
                sb.append(TAB).append(entityType.getDescription().getString()).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        context.getSource().sendSystemMessage(Component.nullToEmpty(sb.toString()));
        return 1;
    }


    private static int makeNormal(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayer();
        if (executor == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("this command must be executed by a player"));
            return 0;
        }
        double ownedShops = Shopkeepers.getData().getShopkeeperData().values().stream()
                .filter(sd -> sd.owners().contains(executor.getUUID()))
                .mapToDouble(sd -> 1.0 / sd.owners().size())
                .sum();
        int maxShops = Shopkeepers.getData().getMaxOwnedShops();
        String maxShopOwner = "the";
        if(Shopkeepers.getData().getPlayerMaxOwnedShops().containsKey(executor.getUUID())) {
            maxShops = Shopkeepers.getData().getPlayerMaxOwnedShops().get(executor.getUUID());
            maxShopOwner = "your";
        }
        if (ownedShops >= maxShops) {
            String relationshipDescription = (ownedShops == maxShops) ? "at" : "greater than";
            if(Shopkeepers.isAdmin(executor)) {
                executor.sendSystemMessage(Component.nullToEmpty(String.format("Warning: you already own %.2f current shops (adjusted for multiple owners)," +
                        " %s %s normal max of %d. Since you are a shopkeepers administrator," +
                        " you are allowed to bypass the limit. Continuing with shop creation.",
                        ownedShops, relationshipDescription, maxShopOwner, maxShops)));
            } else {
                executor.sendSystemMessage(Component.nullToEmpty(String.format("You already own %.2f current shops (adjusted for multiple owners)," +
                                " %s %s max of %d. Disband another shop(s) if you want to make a new one",
                        ownedShops, relationshipDescription, maxShopOwner, maxShops)));
                return 0;
            }
        }
        return make(context, false);
    }

    private static int makeAdmin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return make(context, true);
    }

    private static int make(CommandContext<CommandSourceStack> context, boolean isAdmin) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayer();
        if (executor == null) {
            context.getSource().sendSystemMessage(Component.nullToEmpty("this command must be executed by a player"));
            return 0;
        }
        try {
            Holder.Reference<EntityType<?>>
                    entity = ResourceArgument.getSummonableEntityType(context, "entity");
            if(entity == null) {
                Identifier villagerId = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VILLAGER);
                entity = BuiltInRegistries.ENTITY_TYPE.getValue(villagerId).builtInRegistryHolder();
            }

            if(!Shopkeepers.getData().getAllowedShopkeepers().contains(entity.value())) {
                context.getSource().sendSystemMessage(Component.nullToEmpty("That's not an approved shopkeeper entity"));
                return 0;
            }

            Entity shopkeeper = ShopkeeperEntitySummoner.summon(context.getSource(), entity, executor.position(),
                    executor.onGround(), true);
            shopkeeper.teleportTo(executor.level(), executor.getX(), executor.getY(), executor.getZ(),
                    new HashSet<>(), executor.getYRot(), executor.getXRot(), true);

            List<UUID> owners = new ArrayList<>();
            if(!isAdmin) {
                owners.add(executor.getUUID());
            }

            Shopkeepers.getData().getShopkeeperData().put(shopkeeper.getUUID(), new ShopkeeperData(new ArrayList<>(),
                    new ArrayList<>(), isAdmin, owners));
            Shopkeepers.getData().setDirty();
            return 1;
        } catch (Exception e) {
            Shopkeepers.LOGGER.error("Error making shopkeeper", e);
            return 0;
        }

    }

}
