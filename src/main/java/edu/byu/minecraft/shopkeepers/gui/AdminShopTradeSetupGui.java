package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AdminShopTradeSetupGui extends TradeSetupGui {


    public AdminShopTradeSetupGui(ServerPlayerEntity player, Entity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X5, player, shopkeeper, 45);
        this.setTitle(Text.of("Admin shop setup"));
    }

    protected void setupSlots() {
        setupPage(0);

        setName(37);
        mobOptions(38);
        this.setSlot(39, GuiUtils.EMPTY_SLOT);
        disbandShopkeeper(40);
        this.setSlot(41, GuiUtils.EMPTY_SLOT);
        this.setSlot(42, GuiUtils.EMPTY_SLOT);
        openTradeMenu(43);
    }

    private void setupPage(int page) {
        for(int i = 0; i < 27; i++) {
            this.setSlotRedirect(i, new Slot(tradeItems, (27 * page) + i, 0, 0));
        }

        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        for(int i = 0; i < 9; i++) {
            showUses(27 + i, (9 * page) + i, true);
        }

        this.setSlot(36, page > 0 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Previous Page")).setCallback(() -> setupPage(page - 1)).build() :
                GuiUtils.EMPTY_SLOT);

        this.setSlot(44, page < 4 ?
                new GuiElementBuilder(Items.ARROW).setItemName(Text.of("Next Page")).setCallback(() -> setupPage(page + 1)).build() :
                GuiUtils.EMPTY_SLOT);
    }
}
