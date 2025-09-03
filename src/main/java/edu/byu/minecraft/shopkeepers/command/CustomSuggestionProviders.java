package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
}
