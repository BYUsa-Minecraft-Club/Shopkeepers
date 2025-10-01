package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

    private static final Set<EntityType<?>> LIVING_ENTITIES;
    private static final Set<EntityType<?>> SIDE_EFFECT_ENTITIES =
            Set.of(EntityType.ENDER_DRAGON, EntityType.WITHER);
    /*Ender dragons are not interactable and withers show a boss bar. I don't want either of those, so I'm just not
    going to make them options in the first place. Also players aren't summonable (use a mannequin instead) */

    static {
        LIVING_ENTITIES = Arrays.stream(EntityType.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType() == EntityType.class)
                .filter(field -> LivingEntity.class.isAssignableFrom(
                        (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]))
                .map((Function<Field, EntityType<?>>) field -> {
                    field.setAccessible(true);
                    try {
                        return (EntityType<?>) field.get(null); //null because the field is static
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(type -> !SIDE_EFFECT_ENTITIES.contains(type))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static CompletableFuture<Suggestions> unaddedEntityTypes(CommandContext<ServerCommandSource> ctx,
                                                   SuggestionsBuilder builder) {
        LIVING_ENTITIES.stream()
                .filter(type -> !Shopkeepers.getData().getAllowedShopkeepers().contains(type))
                .map(EntityType::getUntranslatedName)
                .filter(s -> CommandSource.shouldSuggest(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
