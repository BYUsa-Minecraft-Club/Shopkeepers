package edu.byu.minecraft.shopkeepers.gui;

import com.mojang.authlib.GameProfile;
import edu.byu.minecraft.Shopkeepers;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.UUID;

public class GuiUtils {

    static final GuiElement EMPTY_SLOT = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).hideTooltip().hideDefaultTooltip().build();

    static boolean ensureInteractionLock(ServerPlayerEntity player, Entity shopkeeper) {
        UUID lockedPlayer = Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUuid(), player.getUuid());
        boolean locked = lockedPlayer != null && !lockedPlayer.equals(player.getUuid());
        if(locked) {
            player.sendMessage(Text.of(Shopkeepers.getData().getPlayers().get(lockedPlayer) +
                    " is currently interacting with this shopkeeper. Please wait for them to finish."));
        }
        return !locked;
    }

    static ItemStack getPlayerHead(UUID uuid) {
        ItemStack playerHead = Items.PLAYER_HEAD.getDefaultStack();
        playerHead.set(DataComponentTypes.PROFILE, ProfileComponent.ofDynamic(uuid));
        return playerHead;
    }
}
