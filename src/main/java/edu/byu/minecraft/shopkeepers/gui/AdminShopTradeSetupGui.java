package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.TradeData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerProfession;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdminShopTradeSetupGui extends TradeSetupGui {


    public AdminShopTradeSetupGui(ServerPlayerEntity player, Entity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X5, player, shopkeeper, 45);
        this.setTitle(Text.of("Admin shop setup"));
    }

    protected void setupSlots() {
        setupPage(0);

        babyToggle(37);
        setName(38);
        mobOptions(39);
        this.setSlot(40, GuiUtils.EMPTY_SLOT);
        disbandShopkeeper(41);
        this.setSlot(42, GuiUtils.EMPTY_SLOT);
        openTradeMenu(43);
    }

    private void setupPage(int page) {
        for(int i = 0; i < 27; i++) {
            this.setSlotRedirect(i, new Slot(tradeItems, (27 * page) + i, 0, 0));
        }

        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        for(int i = 0; i < 9; i++) {
            int trade = (9 * page) + i;
            if(shopkeeperData.trades().size() > trade && shopkeeperData.trades().get(trade) != null) {
                TradeData tradeData = shopkeeperData.trades().get(trade);
                List<Text> users = tradeData.uses().entrySet().stream()
                        .map(entry -> Map.entry(Shopkeepers.getData().getPlayers().get(entry.getKey()),
                                entry.getValue())).sorted((o1, o2) -> {
                            if (Objects.equals(o1.getValue(), o2.getValue())) return o1.getKey().compareTo(o2.getKey());
                            return o2.getValue().compareTo(o1.getValue());
                        }).map(entry -> Text.of(String.format("%s: %d", entry.getKey(), entry.getValue())))
                        .toList();
                int totalUses = tradeData.uses().values().stream().mapToInt(v -> v).sum();

                setSlot(27 + i, new GuiElementBuilder(totalUses == 0 ? Items.BOOK : Items.WRITTEN_BOOK)
                        .setItemName(Text.of(String.format("Trade used %d times", totalUses)))
                        .setLore(users));
            } else {
                setSlot(27 + i, new GuiElementBuilder(Items.PAPER).setItemName(Text.of("Create trade to track uses")));
            }
        }

        this.setSlot(36, page > 0 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Previous Page")).setCallback(() -> setupPage(page - 1)).build() :
                GuiUtils.EMPTY_SLOT);

        this.setSlot(44, page < 4 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Next Page")).setCallback(() -> setupPage(page + 1)).build() :
                GuiUtils.EMPTY_SLOT);
    }
}
