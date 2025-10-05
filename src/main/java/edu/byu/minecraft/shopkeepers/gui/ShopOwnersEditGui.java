package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.*;

public class ShopOwnersEditGui extends SimpleGui {

    private static final int MAX_SHOP_OWNERS = 20;

    private final SimpleGui parent;

    private final UUID shopkeeperId;

    public ShopOwnersEditGui(ServerPlayerEntity player, UUID shopkeeperId, SimpleGui parent) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.parent = parent;
        this.shopkeeperId = shopkeeperId;
        setTitle(Text.of("Shopkeeper Owners"));
    }

    @Override
    public void onOpen() {
        super.onOpen();
        setupSlots();
    }


    private void setupSlots() {
        ShopkeeperData data = Shopkeepers.getData().getShopkeeperData().get(shopkeeperId);

        boolean isAdmin = Shopkeepers.isAdmin(player);

        if (!isAdmin && !data.owners().contains(player.getUuid())) {
            close();
            return;
        }

        TreeMap<String, UUID> ownersMap = new TreeMap<>();
        for(UUID ownerId : data.owners()) {
            String username = Shopkeepers.getData().getPlayers().get(ownerId);
            if(username != null) {
                ownersMap.put(username, ownerId);
            }
        }

        int index = 0;
        for(Map.Entry<String, UUID> entry : ownersMap.entrySet()) {
            ItemStack playerHead = GuiUtils.getPlayerHead(entry.getValue());
            Style style = Style.EMPTY.withItalic(true);

            GuiElementBuilder guiBuilder = GuiElementBuilder.from(playerHead).setName(Text.of(entry.getKey()));
            if(ownersMap.size() > 1) {
                guiBuilder = guiBuilder.setLore(List.of(Text.empty(),
                                Text.of("Click to remove").getWithStyle(style).getFirst(), Text.empty()))
                        .setCallback(() -> {
                            data.owners().remove(entry.getValue());
                            Shopkeepers.getData().markDirty();
                            if (entry.getValue().equals(player.getUuid())) {
                                if (isAdmin) {
                                    player.sendMessage(
                                            Text.of("Warning: you just removed yourself. Now editing as administrator"));
                                    setupSlots();
                                } else {
                                    close();
                                }
                            } else {
                                setupSlots();
                            }
                        });
            }
            else {
                guiBuilder = guiBuilder.setLore(List.of(Text.empty(),
                        Text.of("You cannot remove the only owner").getWithStyle(style).getFirst(), Text.empty()));
            }
            setSlot(index, guiBuilder.build());
            index++;
        }

        if(data.owners().size() < MAX_SHOP_OWNERS) {
            setSlot(ownersMap.size(), new GuiElementBuilder(Items.LIME_DYE).setName(Text.of("Add Owner"))
                    .setCallback(() -> new TextInputGui(player, "username", (username) -> {
                        for (Map.Entry<UUID, String> entry : Shopkeepers.getData().getPlayers().entrySet()) {
                            if (username.equalsIgnoreCase(entry.getValue())) {
                                data.owners().add(entry.getKey());
                                Shopkeepers.getData().markDirty();
                                return;
                            }
                        }
                        player.sendMessage(Text.of("User with username " + username + " not found"));
                    }, this).open()));
        }

        for(int i = ownersMap.size() + 1; i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setName(Text.of("Close")).setCallback(() -> {
            this.close();
            parent.open();
        }).build());

    }
}
