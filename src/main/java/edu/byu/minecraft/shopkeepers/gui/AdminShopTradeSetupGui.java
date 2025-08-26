package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerProfession;

import java.util.UUID;

public class AdminShopTradeSetupGui extends TradeSetupGui {


    public AdminShopTradeSetupGui(ServerPlayerEntity player, Entity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X4, player, shopkeeper, 45);
        this.setTitle(Text.of("Admin shop setup"));
    }

    protected void setupSlots() {
        setupPage(0);

        this.setSlot(34, new GuiElementBuilder(Items.LIGHT).setItemName(Text.of("Open Trade Menu")).setCallback(() -> {
            this.close();
            new OfferGui(player, shopkeeper).open();
        }).build());
    }

    private void setupPage(int page) {
        for(int i = 0; i < 27; i++) {
            this.setSlotRedirect(i, new Slot(tradeItems, (27 * page) + i, 0, 0));
        }

        this.setSlot(27, page > 0 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Previous Page")).setCallback(() -> setupPage(page - 1)).build() :
                GuiUtils.EMPTY_SLOT);

        this.setSlot(35, page < 4 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Next Page")).setCallback(() -> setupPage(page + 1)).build() :
                GuiUtils.EMPTY_SLOT);

        babyToggle(28);
        setName(29);
        disbandShopkeeper(31);
    }
}
