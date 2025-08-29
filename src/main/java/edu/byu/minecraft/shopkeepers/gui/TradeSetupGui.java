package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.customization.CustomizationButtonOptions;
import edu.byu.minecraft.shopkeepers.customization.CustomizationManager;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.TradeData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public abstract class TradeSetupGui extends SimpleGui {

    protected final Entity shopkeeper;
    protected final SimpleInventory tradeItems;
    protected final int maxTrades;

    public TradeSetupGui(ScreenHandlerType<?> type, ServerPlayerEntity player, Entity shopkeeper, int maxTrades) {
        super(type, player, false);
        this.shopkeeper = shopkeeper;
        this.maxTrades = maxTrades;
        tradeItems = new SimpleInventory(((int) Math.ceil(maxTrades / 9.0)) * 27);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getData().get(shopkeeper.getUuid());
        List<TradeData> trades = shopkeeperData.trades();
        for (int i = 0; i < trades.size(); i++) {
            int page = i / 9;
            int indexOnPage = i % 9;
            int index = (27 * page) + indexOnPage;
            TradeData tradeOffer = trades.get(i);
            tradeItems.setStack(index, tradeOffer.firstBuyItem());
            if (tradeOffer.secondBuyItem().isPresent()) {
                tradeItems.setStack(index + 9, tradeOffer.secondBuyItem().get());
            }
            tradeItems.setStack(index + 18, tradeOffer.sellItem());
        }

        setupSlots();
    }

    protected abstract void setupSlots();

    protected void babyToggle(int slot) {
        MobEntity asMob = (MobEntity) shopkeeper;
        boolean currentlyBaby = asMob.isBaby();
        GuiElementBuilder builder = currentlyBaby ?
                new GuiElementBuilder(Items.SLIME_BALL).setItemName(Text.of("Change to adult")) :
                new GuiElementBuilder(Items.SLIME_BLOCK).setItemName(Text.of("Change to baby (if applicable to mob)"));
        builder.setCallback(() -> {
            asMob.setBaby(!currentlyBaby);
            if(!currentlyBaby && shopkeeper instanceof PassiveEntity pe) {
                pe.setBreedingAge(-2099999999); //about 3.4 years of loaded time
            }
            babyToggle(slot);
        });
        this.setSlot(slot, builder.build());
    }

    protected void setName(int slot) {
        GuiElementBuilder builder = new GuiElementBuilder(Items.NAME_TAG).setItemName(Text.of("Change name"));
        if (shopkeeper.getCustomName() != null) {
            builder.setLore(List.of(Text.of("Current name:"), shopkeeper.getName()));
        }
        builder.setCallback(() -> new TextInputGui(player, shopkeeper.getName().getString(), (str) -> {
            shopkeeper.setCustomName(Text.of(str));
            setName(slot);
        }, this).open());
        this.setSlot(slot, builder.build());
    }

    protected void mobOptions(int slot) {
        CustomizationButtonOptions<?> buttonOptions =
                CustomizationManager.getCustomizationButtonOptions(shopkeeper, player, this);
        if (buttonOptions != null) {
            setSlot(slot, new GuiElementBuilder(buttonOptions.mobSpawnEgg())
                    .setItemName(Text.of("Edit " + buttonOptions.mobName() + " Options"))
                    .setCallback(() -> buttonOptions.settingsGui().open()).build());
        } else {
            setSlot(slot, GuiUtils.EMPTY_SLOT);
        }
    }


    protected void openTradeMenu(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BELL).setItemName(Text.of("Open Trade Menu")).setCallback(() -> {
            this.close();
            new OfferGui(player, shopkeeper).open();
        }).build());
    }

    protected void disbandShopkeeper(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BARRIER).setItemName(Text.of("Disband Shopkeeper")).setCallback(() -> {
                    new ConfirmationGui(player, "disband shopkeeper", this, () -> {
                        ShopkeeperData shopkeeperData = Shopkeepers.getData().getData().get(shopkeeper.getUuid());

                        for(TradeData tradeData : shopkeeperData.trades()) {
                            if(tradeData.firstBuyItem() != null && !tradeData.firstBuyItem().isEmpty()) {
                                player.giveOrDropStack(tradeData.firstBuyItem());
                            }
                            if(tradeData.secondBuyItem().isPresent() && !tradeData.secondBuyItem().get().isEmpty()) {
                                player.giveOrDropStack(tradeData.secondBuyItem().get());
                            }
                            if(tradeData.sellItem() != null && !tradeData.sellItem().isEmpty()) {
                                player.giveOrDropStack(tradeData.sellItem());
                            }
                        }

                        for(ItemStack itemStack : shopkeeperData.inventory()) {
                            ItemStack copy = itemStack.copy();
                            while(copy.getCount() > copy.getMaxCount()) {
                                ItemStack copy2 = copy.copy();
                                copy2.setCount(copy.getMaxCount());
                                copy.setCount(copy.getCount() - copy2.getCount());
                                player.giveOrDropStack(copy2);
                            }
                            player.giveOrDropStack(copy);
                        }

                        Shopkeepers.getData().getData().remove(shopkeeper.getUuid());
                        shopkeeper.setPosition(shopkeeper.getX(), -1000, shopkeeper.getZ());
                        shopkeeper.kill(player.getWorld());
                    }).open();
                }).build());
    }



    @Override
    public void onClose() {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getData().get(shopkeeper.getUuid());
        boolean changed = false;
        for(int i = 0; i < maxTrades; i++) {
            int page = i / 9;
            int indexOnPage = i % 9;
            int index = (27 * page) + indexOnPage;
            TradeData newTrade = validateTradeOrGiveBack(index);
            if(newTrade != null) {
                if (shopkeeperData.trades().size() <= i) {
                    shopkeeperData.trades().add(newTrade);
                    changed = true;
                } else if (!shopkeeperData.trades().get(i).equalsIgnoreUses(newTrade)) {
                    shopkeeperData.trades().set(i, newTrade);
                    changed = true;
                }
            }
            else if (shopkeeperData.trades().size() > i && shopkeeperData.trades().get(i) != null) {
                shopkeeperData.trades().set(i, null);
                changed = true;
            }
        }
        if(changed) {
            shopkeeperData.trades().removeIf(Objects::isNull);
            Shopkeepers.getData().markDirty();
        }
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid());
    }

    protected TradeData validateTradeOrGiveBack(int i) {
        boolean firstBuyItemPresent = !tradeItems.getStack(i).isEmpty();
        boolean secondBuyItemPresent = !tradeItems.getStack(i + 9).isEmpty();
        boolean sellItemPresent = !tradeItems.getStack(i + 18).isEmpty();
        if (firstBuyItemPresent && sellItemPresent) {
            ItemStack firstBuyItem = tradeItems.getStack(i);
            Optional<ItemStack> secondBuyItem =
                    secondBuyItemPresent ? Optional.of(tradeItems.getStack(i + 9)) : Optional.empty();
            ItemStack sellItem = tradeItems.getStack(i + 18);
            return new TradeData(firstBuyItem, secondBuyItem, sellItem, new HashMap<>());
        }
        else {
            if(firstBuyItemPresent) {
                player.giveOrDropStack(tradeItems.getStack(i));
            }
            if(secondBuyItemPresent) {
                player.giveOrDropStack(tradeItems.getStack(i + 9));
            }
            if(sellItemPresent) {
                player.giveOrDropStack(tradeItems.getStack(i + 18));
            }
            return null;
        }
    }

}
