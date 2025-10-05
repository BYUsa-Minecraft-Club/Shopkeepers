package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerShopTradeSetupGui extends TradeSetupGui {


    public PlayerShopTradeSetupGui(ServerPlayerEntity player, LivingEntity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X5, player, shopkeeper, 8);
        this.setTitle(Text.of("Player shop setup"));
    }

    protected void setupSlots() {
        for(int i = 0; i < 27; i++) {
            if(i % 9 == 8) continue;
            this.setSlotRedirect(i, new Slot(tradeItems, i, 0, 0));
        }

        this.setSlot(8, new GuiElementBuilder(Items.YELLOW_STAINED_GLASS_PANE).setItemName(Text.of("← Price #1")).build());
        this.setSlot(17, new GuiElementBuilder(Items.ORANGE_STAINED_GLASS_PANE).setItemName(Text.of("← Price #2 (Optional)")).build());
        this.setSlot(26, new GuiElementBuilder(Items.LIME_STAINED_GLASS_PANE).setItemName(Text.of("← Product ")).build());

        for(int i = 0; i < 8; i++) {
            showUses(27 + i, i, false);
        }
        setSlot(35, GuiUtils.EMPTY_SLOT);

        setName(36);
        boolean appearanceOptionsPresent = appearanceOptions(37);
        equipmentOptions(appearanceOptionsPresent ? 38 : 37);
        if(!appearanceOptionsPresent) {
            setSlot(38, GuiUtils.EMPTY_SLOT);
        }

        setSlot(39, GuiUtils.EMPTY_SLOT);
        disbandShopkeeper(40);
        teleportShopkeeper(41);

        setSlot(42, GuiElementBuilder.from(GuiUtils.getPlayerHead(player.getUuid()))
                .setName(Text.of("Edit Shopkeeper Owners"))
                .setCallback(() -> new ShopOwnersEditGui(player, shopkeeper.getUuid(), this).open())
                .build());
        setSlot(43, new GuiElementBuilder(Items.CHEST)
                .setItemName(Text.of("Open Shopkeeper Inventory"))
                .setCallback(() -> {
                    this.close();
                    new MerchantInventoryGui(player, shopkeeper).open();
                })
                .build());

        openTradeMenu(44);
    }


}
