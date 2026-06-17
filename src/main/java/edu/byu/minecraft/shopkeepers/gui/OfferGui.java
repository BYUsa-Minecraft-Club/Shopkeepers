package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.TradeData;
import eu.pb4.sgui.api.gui.MerchantGui;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public class OfferGui extends MerchantGui {

    private static final ItemCost adminEditTradeBuyItem;
    private static final ItemStack adminEditTradeSellItem;
    private static final ItemCost ownerEditTradeBuyItem;
    private static final ItemStack ownerEditTradeSellItem;

    static {
        TypedDataComponent<Component> adminNameComponenet = TypedDataComponent.createUnchecked(DataComponents.CUSTOM_NAME, Component.nullToEmpty("Admin: Edit Shopkeeper"));
        TypedDataComponent<ItemLore> adminLoreComponent = TypedDataComponent.createUnchecked(DataComponents.LORE, new ItemLore(List.of(Component.nullToEmpty("Admins only"))));
        adminEditTradeBuyItem = new ItemCost(Items.PAPER.builtInRegistryHolder(), 1, DataComponentExactPredicate.builder().expect(adminNameComponenet).expect(adminLoreComponent).build());
        adminEditTradeSellItem = new ItemStack(Items.PAPER.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(adminNameComponenet).set(adminLoreComponent).build());
        
        TypedDataComponent<Component> ownerNameComponenet = TypedDataComponent.createUnchecked(DataComponents.CUSTOM_NAME, Component.nullToEmpty("Shopkeeper Owner: Edit Shopkeeper"));
        TypedDataComponent<ItemLore> ownerLoreComponent = TypedDataComponent.createUnchecked(DataComponents.LORE, new ItemLore(List.of(Component.nullToEmpty("shopkeeper owners only"))));
        ownerEditTradeBuyItem = new ItemCost(Items.PAPER.builtInRegistryHolder(), 1, DataComponentExactPredicate.builder().expect(ownerNameComponenet).expect(ownerLoreComponent).build());
        ownerEditTradeSellItem = new ItemStack(Items.PAPER.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(ownerNameComponenet).set(ownerLoreComponent).build());
    }

    private final LivingEntity shopkeeper;
    private boolean isAdmin = false;
    private boolean isOwner = false;

    public OfferGui(ServerPlayer player, LivingEntity shopkeeper) {
        super(player, false);
        this.shopkeeper = shopkeeper;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());

        if(shopkeeperData.owners().contains(player.getUUID())) {
            isOwner = true;
            addTrade(new MerchantOffer(ownerEditTradeBuyItem, ownerEditTradeSellItem, 1, 0, 0));
        } else if(Shopkeepers.isAdmin(player)) {
            isAdmin = true;
            addTrade(new MerchantOffer(adminEditTradeBuyItem, adminEditTradeSellItem, 1, 0, 0));
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
    public void onSelectTrade(MerchantOffer offer) {
        super.onSelectTrade(offer);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());
        if(isAdmin && ItemStack.matches(offer.getResult(), adminEditTradeSellItem) &&
                offer.getItemCostA().test(adminEditTradeSellItem)) {
            this.close();
            if(shopkeeperData.isAdmin()) {
                new AdminShopTradeSetupGui(player, shopkeeper).open();
            } else {
                new PlayerShopTradeSetupGui(player, shopkeeper).open();
            }
        } else if(isOwner && ItemStack.matches(offer.getResult(), ownerEditTradeSellItem) &&
                offer.getItemCostA().test(ownerEditTradeSellItem)) {
            this.close();
            new PlayerShopTradeSetupGui(player, shopkeeper).open();
        }
    }

    @Override
    public boolean onTrade(MerchantOffer offer) { //method should be called something more like "should trade", but it's
        // from sgui so I can't change it myself
        ShopkeeperData data = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());
        return data.isAdmin() || data.inventory().stream().anyMatch(
                entry -> ItemStack.isSameItemSameComponents(entry.getStack(), offer.getResult()) &&
                        entry.getAmount() >= offer.getResult().getCount());
    }

    public void afterTrade(MerchantOffer offer) {
        int index = getOfferIndex(offer);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());

        if(!shopkeeperData.isAdmin()) {
            shopkeeperData.addItems(offer.getItemCostA().itemStack(), offer.getItemCostA().count());
            if (offer.getItemCostB().isPresent()) {
                shopkeeperData.addItems(offer.getItemCostB().get().itemStack(),
                        offer.getItemCostB().get().count());
            }
            shopkeeperData.removeItems(offer.getResult(), offer.getResult().getCount());

            boolean changedTrades = false;
            for (int i = 0; i < shopkeeperData.trades().size(); i++) {
                MerchantOffer tradeOffer = this.merchant.getOffers().get(i);
                if (tradeOffer == offer) continue;
                MerchantOffer modifiedOffer = getOfferWithCorrectMaxUses(shopkeeperData.trades().get(i), shopkeeperData);
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
        uses.put(player.getUUID(), uses.getOrDefault(player.getUUID(), 0) + 1);

        Shopkeepers.getData().setDirty();
    }

    private MerchantOffer getOfferWithCorrectMaxUses(TradeData base, ShopkeeperData shopkeeperData) {
        Integer itemsOwned = shopkeeperData.inventoryGet(base.sellItem());
        int maxUses = itemsOwned == null ? 0 : itemsOwned / base.sellItem().getCount();
        if(shopkeeperData.isAdmin()) {
            maxUses = Integer.MAX_VALUE;
        }
        ItemCost firstTradedItem = new ItemCost(base.firstBuyItem().getItemHolder(), base.firstBuyItem().getCount(),
                DataComponentExactPredicate.allOf(base.firstBuyItem().getComponents()));
        Optional<ItemCost> secondTradedItem = base.secondBuyItem().map(stack ->
                new ItemCost(stack.getItemHolder(), stack.getCount(), DataComponentExactPredicate.allOf(stack.getComponents())));
        return new MerchantOffer(firstTradedItem, secondTradedItem, base.sellItem(), maxUses, 0, 0);
    }

    @Override
    public void onScreenHandlerClosed() {
        super.onScreenHandlerClosed();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUUID(), player.getUUID());
    }
}
