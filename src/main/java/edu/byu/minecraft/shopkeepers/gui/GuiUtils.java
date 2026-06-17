package edu.byu.minecraft.shopkeepers.gui;

import com.mojang.authlib.GameProfile;
import edu.byu.minecraft.Shopkeepers;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

public class GuiUtils {

    static final GuiElement EMPTY_SLOT = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).hideTooltip().hideDefaultTooltip().build();

    static boolean ensureInteractionLock(ServerPlayer player, Entity shopkeeper) {
        UUID lockedPlayer = Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUUID(), player.getUUID());
        boolean locked = lockedPlayer != null && !lockedPlayer.equals(player.getUUID());
        if(locked) {
            player.sendSystemMessage(Component.nullToEmpty(Shopkeepers.getData().getPlayers().get(lockedPlayer) +
                    " is currently interacting with this shopkeeper. Please wait for them to finish."));
        }
        return !locked;
    }

    static ItemStack getPlayerHead(UUID uuid) {
        ItemStack playerHead = Items.PLAYER_HEAD.getDefaultInstance();
        playerHead.set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(uuid));
        return playerHead;
    }
}
