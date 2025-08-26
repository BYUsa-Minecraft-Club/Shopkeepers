package edu.byu.minecraft.shopkeepers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import edu.byu.minecraft.Shopkeepers;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class CustomSuggestionProviders {
    public static CompletableFuture<Suggestions> allPlayers(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
        Shopkeepers.getData().getPlayers().values().forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> approvedShopkeeperEntities(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
        Shopkeepers.getData().getAllowedShopkeepers().stream().map(EntityType::getUntranslatedName)
                .filter(s -> CommandSource.shouldSuggest(builder.getRemaining(), s))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
