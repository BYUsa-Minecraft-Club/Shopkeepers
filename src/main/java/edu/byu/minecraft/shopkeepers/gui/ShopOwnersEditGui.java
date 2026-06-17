package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ShopOwnersEditGui extends SimpleGui {

    private static final int MAX_SHOP_OWNERS = 20;

    private final SimpleGui parent;

    private final UUID shopkeeperId;

    public ShopOwnersEditGui(ServerPlayer player, UUID shopkeeperId, SimpleGui parent) {
        super(MenuType.GENERIC_9x3, player, false);
        this.parent = parent;
        this.shopkeeperId = shopkeeperId;
        setTitle(Component.nullToEmpty("Shopkeeper Owners"));
    }

    @Override
    public void onOpen() {
        super.onOpen();
        setupSlots();
    }


    private void setupSlots() {
        ShopkeeperData data = Shopkeepers.getData().getShopkeeperData().get(shopkeeperId);

        boolean isAdmin = Shopkeepers.isAdmin(player);

        if (!isAdmin && !data.owners().contains(player.getUUID())) {
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

            GuiElementBuilder guiBuilder = GuiElementBuilder.from(playerHead).setName(Component.nullToEmpty(entry.getKey()));
            if(ownersMap.size() > 1) {
                guiBuilder = guiBuilder.setLore(List.of(Component.empty(),
                                Component.nullToEmpty("Click to remove").toFlatList(style).getFirst(), Component.empty()))
                        .setCallback(() -> {
                            data.owners().remove(entry.getValue());
                            Shopkeepers.getData().setDirty();
                            if (entry.getValue().equals(player.getUUID())) {
                                if (isAdmin) {
                                    player.sendSystemMessage(
                                            Component.nullToEmpty("Warning: you just removed yourself. Now editing as administrator"));
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
                guiBuilder = guiBuilder.setLore(List.of(Component.empty(),
                        Component.nullToEmpty("You cannot remove the only owner").toFlatList(style).getFirst(), Component.empty()));
            }
            setSlot(index, guiBuilder.build());
            index++;
        }

        if(data.owners().size() < MAX_SHOP_OWNERS) {
            setSlot(ownersMap.size(), new GuiElementBuilder(Items.LIME_DYE).setName(Component.nullToEmpty("Add Owner"))
                    .setCallback(() -> new TextInputGui(player, "username", (username) -> {
                        for (Map.Entry<UUID, String> entry : Shopkeepers.getData().getPlayers().entrySet()) {
                            if (username.equalsIgnoreCase(entry.getValue())) {
                                data.owners().add(entry.getKey());
                                Shopkeepers.getData().setDirty();
                                return;
                            }
                        }
                        player.sendSystemMessage(Component.nullToEmpty("User with username " + username + " not found"));
                    }, this).open()));
        }

        for(int i = ownersMap.size() + 1; i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setName(Component.nullToEmpty("Close")).setCallback(() -> {
            this.close();
            parent.open();
        }).build());

    }
}
