package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import edu.byu.minecraft.Shopkeepers;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;

public class CustomSuggestionProviders {
    public static CompletableFuture<Suggestions> allPlayers(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        List<String> onlinePlayers = List.of(ctx.getSource().getServer().getPlayerList().getPlayerNamesArray());
        Map<Boolean, List<String>> hi = Shopkeepers.getData().getPlayers().values().stream()
                .filter(s -> SharedSuggestionProvider.matchesSubStr(builder.getRemaining().toLowerCase(), s.toLowerCase()))
                .collect(Collectors.partitioningBy(onlinePlayers::contains));
        List<String> suggestions = hi.get(!hi.get(true).isEmpty());
        suggestions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> approvedShopkeeperEntities(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        Shopkeepers.getData().getAllowedShopkeepers().stream().map(EntityType::toShortString)
                .filter(s -> SharedSuggestionProvider.matchesSubStr(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static final Set<EntityType<?>> LIVING_ENTITIES;
    private static final Set<EntityType<?>> SIDE_EFFECT_ENTITIES =
            Set.of(EntityTypes.ENDER_DRAGON, EntityTypes.WITHER);
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

    public static CompletableFuture<Suggestions> unaddedEntityTypes(CommandContext<CommandSourceStack> ctx,
                                                   SuggestionsBuilder builder) {
        LIVING_ENTITIES.stream()
                .filter(type -> !Shopkeepers.getData().getAllowedShopkeepers().contains(type))
                .map(EntityType::toShortString)
                .filter(s -> SharedSuggestionProvider.matchesSubStr(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
