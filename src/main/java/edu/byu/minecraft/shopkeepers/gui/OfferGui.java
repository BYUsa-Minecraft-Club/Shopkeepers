package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.TradeData;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OfferGui extends MerchantGui {

    private static final TradedItem adminEditTradeBuyItem;
    private static final ItemStack adminEditTradeSellItem;
    private static final TradedItem ownerEditTradeBuyItem;
    private static final ItemStack ownerEditTradeSellItem;

    static {
        Component<Text> adminNameComponenet = Component.of(DataComponentTypes.CUSTOM_NAME, Text.of("Admin: Edit Shopkeeper"));
        Component<LoreComponent> adminLoreComponent = Component.of(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("Admins only"))));
        adminEditTradeBuyItem = new TradedItem(Items.PAPER.getRegistryEntry(), 1, ComponentMapPredicate.builder().add(adminNameComponenet).add(adminLoreComponent).build());
        adminEditTradeSellItem = new ItemStack(Items.PAPER.getRegistryEntry(), 1, ComponentChanges.builder().add(adminNameComponenet).add(adminLoreComponent).build());
        
        Component<Text> ownerNameComponenet = Component.of(DataComponentTypes.CUSTOM_NAME, Text.of("Shopkeeper Owner: Edit Shopkeeper"));
        Component<LoreComponent> ownerLoreComponent = Component.of(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("shopkeeper owners only"))));
        ownerEditTradeBuyItem = new TradedItem(Items.PAPER.getRegistryEntry(), 1, ComponentMapPredicate.builder().add(ownerNameComponenet).add(ownerLoreComponent).build());
        ownerEditTradeSellItem = new ItemStack(Items.PAPER.getRegistryEntry(), 1, ComponentChanges.builder().add(ownerNameComponenet).add(ownerLoreComponent).build());
    }

    private final MobEntity shopkeeper;
    private final boolean isAdmin;
    private final boolean isOwner;

    public OfferGui(ServerPlayerEntity player, MobEntity shopkeeper) {
        super(player, false);
        this.shopkeeper = shopkeeper;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

        if(player.getEntityWorld().getServer().getPlayerManager().isOperator(player.getPlayerConfigEntry())) {
            isAdmin = true;
            isOwner = false;
            addTrade(new TradeOffer(adminEditTradeBuyItem, adminEditTradeSellItem, 1, 0, 0));
        } else if(shopkeeperData.owners().contains(player.getUuid())) {
            isAdmin = false;
            isOwner = true;
            addTrade(new TradeOffer(ownerEditTradeBuyItem, ownerEditTradeSellItem, 1, 0, 0));
        } else {
            isAdmin = false;
            isOwner = false;
        }

        for (TradeData trade : shopkeeperData.trades()) {
            addTrade(getOfferWithCorrectMaxUses(trade, shopkeeperData));
        }
        this.setTitle(shopkeeper.getName());
    }

    @Override
    protected boolean sendGui() {
        if(GuiUtils.ensureInteractionLock(player, shopkeeper)) {
            return super.sendGui();
        }
        else {
            this.close();
            return false;
        }
    }

    @Override
    public void onSelectTrade(TradeOffer offer) {
        super.onSelectTrade(offer);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        if(isAdmin && ItemStack.areEqual(offer.getSellItem(), adminEditTradeSellItem) &&
                offer.getFirstBuyItem().matches(adminEditTradeSellItem)) {
            this.close();
            if(shopkeeperData.isAdmin()) {
                new AdminShopTradeSetupGui(player, shopkeeper).open();
            } else {
                new PlayerShopTradeSetupGui(player, shopkeeper).open();
            }
            Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUuid(), player.getUuid());
        } else if(isOwner && ItemStack.areEqual(offer.getSellItem(), ownerEditTradeSellItem) &&
                offer.getFirstBuyItem().matches(ownerEditTradeSellItem)) {
            this.close();
            new PlayerShopTradeSetupGui(player, shopkeeper).open();
            Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUuid(), player.getUuid());
        }
    }

    @Override
    public boolean onTrade(TradeOffer offer) { //method should be called something more like "should trade", but it's
        // from sgui so I can't change it myself
        ShopkeeperData data = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        return data.isAdmin() || data.inventory().stream().anyMatch(
                entry -> ItemStack.areItemsAndComponentsEqual(entry.getStack(), offer.getSellItem()) &&
                        entry.getAmount() >= offer.getSellItem().getCount());
    }

    public void afterTrade(TradeOffer offer) {
        int index = getOfferIndex(offer);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

        if(!shopkeeperData.isAdmin()) {
            shopkeeperData.addItems(offer.getFirstBuyItem().itemStack(), offer.getFirstBuyItem().count());
            if (offer.getSecondBuyItem().isPresent()) {
                shopkeeperData.addItems(offer.getSecondBuyItem().get().itemStack(),
                        offer.getSecondBuyItem().get().count());
            }
            shopkeeperData.removeItems(offer.getSellItem(), offer.getSellItem().getCount());

            Shopkeepers.getData().markDirty();

            boolean changedTrades = false;
            for (int i = 0; i < shopkeeperData.trades().size(); i++) {
                TradeOffer tradeOffer = this.merchant.getOffers().get(i);
                if (tradeOffer == offer) continue;
                TradeOffer modifiedOffer = getOfferWithCorrectMaxUses(shopkeeperData.trades().get(i), shopkeeperData);
                if (modifiedOffer.getMaxUses() != tradeOffer.getMaxUses() - tradeOffer.getUses()) {
                    this.merchant.getOffers().set((isAdmin || isOwner) ? i + 1 : i, modifiedOffer);
                    changedTrades = true;
                }
            }
            if (changedTrades && this.isOpen() && this.autoUpdate) {
                sendUpdate();
            }
        }

        Map<UUID, Integer> uses = shopkeeperData.trades().get((isAdmin || isOwner) ? index - 1 : index).uses();
        uses.put(player.getUuid(), uses.getOrDefault(player.getUuid(), 0) + 1);
    }

    private TradeOffer getOfferWithCorrectMaxUses(TradeData base, ShopkeeperData shopkeeperData) {
        Integer itemsOwned = shopkeeperData.inventoryGet(base.sellItem());
        int maxUses = itemsOwned == null ? 0 : itemsOwned / base.sellItem().getCount();
        if(shopkeeperData.isAdmin()) {
            maxUses = Integer.MAX_VALUE;
        }
        TradedItem firstTradedItem = new TradedItem(base.firstBuyItem().getRegistryEntry(), base.firstBuyItem().getCount(),
                ComponentMapPredicate.of(base.firstBuyItem().getComponents()));
        Optional<TradedItem> secondTradedItem = base.secondBuyItem().map(stack ->
                new TradedItem(stack.getRegistryEntry(), stack.getCount(), ComponentMapPredicate.of(stack.getComponents())));
        return new TradeOffer(firstTradedItem, secondTradedItem, base.sellItem(), maxUses, 0, 0);
    }

    @Override
    public void onScreenHandlerClosed() {
        super.onScreenHandlerClosed();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid(), player.getUuid());
    }
}
