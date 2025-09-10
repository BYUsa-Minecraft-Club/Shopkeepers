package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomSuggestionProviders {
    public static CompletableFuture<Suggestions> allPlayers(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
        List<String> onlinePlayers = List.of(ctx.getSource().getServer().getPlayerManager().getPlayerNames());
        Map<Boolean, List<String>> hi = Shopkeepers.getData().getPlayers().values().stream()
                .filter(s -> CommandSource.shouldSuggest(builder.getRemaining().toLowerCase(), s.toLowerCase()))
                .collect(Collectors.partitioningBy(onlinePlayers::contains));
        List<String> suggestions = hi.get(!hi.get(true).isEmpty());
        suggestions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> approvedShopkeeperEntities(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
        Shopkeepers.getData().getAllowedShopkeepers().stream().map(EntityType::getUntranslatedName)
                .filter(s -> CommandSource.shouldSuggest(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static final Set<EntityType<?>> MOB_ENTITIES;

    static {
        MOB_ENTITIES = Arrays.stream(EntityType.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType() == EntityType.class)
                .filter(field -> MobEntity.class.isAssignableFrom(
                        (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]))
                .map((Function<Field, EntityType<?>>) field -> {
                    field.setAccessible(true);
                    try {
                        return (EntityType<?>) field.get(null); //null because the field is static
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toCollection(HashSet::new));
    }

    public static CompletableFuture<Suggestions> unaddedEntityTypes(CommandContext<ServerCommandSource> ctx,
                                                   SuggestionsBuilder builder) {
        MOB_ENTITIES.stream()
                .filter(type -> !Shopkeepers.getData().getAllowedShopkeepers().contains(type))
                .map(EntityType::getUntranslatedName)
                .filter(s -> CommandSource.shouldSuggest(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
