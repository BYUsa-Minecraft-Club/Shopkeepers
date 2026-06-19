package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.ShopkeeperMover;
import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.customization.appearance.AppearanceCustomization;
import edu.byu.minecraft.shopkeepers.customization.appearance.AppearanceCustomizationManager;
import edu.byu.minecraft.shopkeepers.customization.equipment.EquipmentCustomization;
import edu.byu.minecraft.shopkeepers.customization.equipment.EquipmentCustomizationManager;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperInventoryEntry;
import edu.byu.minecraft.shopkeepers.data.TradeData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.*;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

public abstract class TradeSetupGui extends SimpleGui {

    protected final LivingEntity shopkeeper;
    protected final SimpleContainer tradeItems;
    protected final int maxTrades;

    public TradeSetupGui(MenuType<?> type, ServerPlayer player, LivingEntity shopkeeper, int maxTrades) {
        super(type, player, false);
        this.shopkeeper = shopkeeper;
        this.maxTrades = maxTrades;
        tradeItems = new SimpleContainer(((int) Math.ceil(maxTrades / 9.0)) * 27);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());
        List<TradeData> trades = shopkeeperData.trades();
        for (int i = 0; i < trades.size(); i++) {
            int page = i / 9;
            int indexOnPage = i % 9;
            int index = (27 * page) + indexOnPage;
            TradeData tradeOffer = trades.get(i);
            tradeItems.setItem(index, tradeOffer.firstBuyItem().copy());
            if (tradeOffer.secondBuyItem().isPresent()) {
                tradeItems.setItem(index + 9, tradeOffer.secondBuyItem().get().copy());
            }
            tradeItems.setItem(index + 18, tradeOffer.sellItem().copy());
        }

        setupSlots();
    }

    protected abstract void setupSlots();

    protected void showUses(int slot, int trade, boolean showPlayerNames) {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());
        if(shopkeeperData.trades().size() > trade && shopkeeperData.trades().get(trade) != null) {
            TradeData tradeData = shopkeeperData.trades().get(trade);

            int totalUses = tradeData.uses().values().stream().mapToInt(v -> v).sum();
            var guiBuilder = new GuiElementBuilder(totalUses == 0 ? Items.BOOK : Items.WRITTEN_BOOK)
                    .setItemName(Component.nullToEmpty(String.format("Trade used %d times", totalUses)));

            if(showPlayerNames) {
                guiBuilder.setLore(tradeData.uses().entrySet().stream()
                        .map(entry -> Map.entry(Shopkeepers.getData().getPlayers().get(entry.getKey()),
                                entry.getValue())).sorted((o1, o2) -> {
                            if (Objects.equals(o1.getValue(), o2.getValue())) return o1.getKey().compareTo(o2.getKey());
                            return o2.getValue().compareTo(o1.getValue());
                        }).map(entry -> Component.nullToEmpty(String.format("%s: %d", entry.getKey(), entry.getValue())))
                        .toList());
            }

            setSlot(slot, guiBuilder.build());
        } else {
            setSlot(slot, new GuiElementBuilder(Items.PAPER).setItemName(Component.nullToEmpty("Create trade to track uses")));
        }
    }

    protected void setName(int slot) {
        GuiElementBuilder builder = new GuiElementBuilder(Items.NAME_TAG).setItemName(Component.nullToEmpty("Change name"));
        if (shopkeeper.getCustomName() != null) {
            builder.setLore(List.of(Component.nullToEmpty("Current name:"), shopkeeper.getName()));
        }
        builder.setCallback(() -> new TextInputGui(player, shopkeeper.getName().getString(), (str) -> {
            shopkeeper.setCustomName(Component.nullToEmpty(str));
            setName(slot);
        }, this).open());
        this.setSlot(slot, builder.build());
    }

    //returns whether there's something important now in the slot
    protected boolean appearanceOptions(int slot) {
        List<AppearanceCustomization<Entity>> appearanceOptions =
                AppearanceCustomizationManager.getAppearanceOptions(shopkeeper, player, this);
        if (appearanceOptions.isEmpty()) {
            setSlot(slot, GuiUtils.EMPTY_SLOT);
            return false;
        } else if (appearanceOptions.size() > 1) {
            Optional<Holder<Item>> optionalEgg = SpawnEggItem.byId(shopkeeper.getType());
            Item egg;
            if(optionalEgg.isEmpty() && shopkeeper instanceof ArmorStand) {
                egg = Items.ARMOR_STAND;
            } else if (optionalEgg.isPresent()) {
                egg = optionalEgg.get().value();
            } else {
                egg = Items.LIME_DYE;
            }
            setSlot(slot, new GuiElementBuilder(egg)
                    .setItemName(Component.nullToEmpty(String.format("Edit %s Appearance Options",
                            CustomizationUtils.capitalize(shopkeeper.getType().getDescription().getString()))))
                    .setCallback(() -> new MobAppearanceGui<>(player, shopkeeper, appearanceOptions, this).open())
                    .build());
            return true;
        } else {
            MobAppearanceGui.setupSlot(this, shopkeeper, slot, appearanceOptions, 0);
            return true;
        }
    }

    protected void equipmentOptions(int slot) {
        List<EquipmentCustomization<LivingEntity>> equipmentOptions =
                EquipmentCustomizationManager.getEquipmentOptions(shopkeeper);
        if(equipmentOptions.isEmpty()) {
            setSlot(slot, GuiUtils.EMPTY_SLOT);
        } else {
            setSlot(slot, new GuiElementBuilder(equipmentOptions.getFirst().getDescriptionItem())
                    .setItemName(Component.nullToEmpty(String.format("Edit %s Equipment Options",
                                    CustomizationUtils.capitalize(shopkeeper.getType().getDescription().getString()))))
                    .setCallback(() -> new MobEquipmentGui<>(player, shopkeeper, equipmentOptions, this).open())
                    .build());
        }

    }

    protected void openTradeMenu(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BELL).setItemName(Component.nullToEmpty("Open Trade Menu")).setCallback(() -> {
            this.close();
            new OfferGui(player, shopkeeper).open();
        }).build());
    }

    protected void teleportShopkeeper(int slot) {
        setSlot(slot, new GuiElementBuilder(Items.ENDER_PEARL)
                .setName(Component.nullToEmpty("Move Shopkeeper"))
                .setCallback(() -> {
                    if(player.getInventory().hasAnyMatching(stack -> {
                        CustomData nbt = stack.getComponents().get(DataComponents.CUSTOM_DATA);
                        return nbt != null && nbt.copyTag().contains(ShopkeeperMover.SHOPKEEPER_ID_KEY);
                    })) {
                        player.sendSystemMessage(Component.nullToEmpty("You already have a teleport item in your inventory"));
                        return;
                    }

                    CompoundTag nbt = new CompoundTag();
                    nbt.putString(ShopkeeperMover.SHOPKEEPER_ID_KEY, shopkeeper.getUUID().toString());

                    String teleportItemName = "Move " + (shopkeeper.hasCustomName() ?
                            Objects.requireNonNull(shopkeeper.getCustomName()).getString() :
                            ("your " + shopkeeper.getName().getString().toLowerCase() + " shopkeeper"));

                    ItemStack teleportItemStack = new ItemStack(Items.ENDER_PEARL.builtInRegistryHolder(), 1,
                            DataComponentPatch.builder()
                                    .set(DataComponents.CUSTOM_DATA, CustomData.of(nbt))
                                    .set(DataComponents.CUSTOM_NAME, Component.nullToEmpty(teleportItemName))
                                    .set(DataComponents.LORE, new ItemLore(List.of(Component.empty(),
                                            Component.nullToEmpty("Use this item to move"), Component.nullToEmpty(teleportItemName.substring(5)),
                                            Component.nullToEmpty("to your current location"), Component.empty())))
                                    .set(DataComponents.MAX_STACK_SIZE, 1)
                                    .build());

                    player.handleExtraItemsCreatedOnUse(teleportItemStack);

                    ShopkeeperMover.addToShopkeeperCache(shopkeeper);
                }));
    }

    protected void disbandShopkeeper(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BARRIER).setItemName(Component.nullToEmpty("Disband Shopkeeper")).setCallback(() -> {
                    new ConfirmationGui(player, "disband shopkeeper", this, () -> {
                        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());

                        for(TradeData tradeData : shopkeeperData.trades()) {
                            if(tradeData.firstBuyItem() != null && !tradeData.firstBuyItem().isEmpty()) {
                                player.handleExtraItemsCreatedOnUse(tradeData.firstBuyItem());
                            }
                            if(tradeData.secondBuyItem().isPresent() && !tradeData.secondBuyItem().get().isEmpty()) {
                                player.handleExtraItemsCreatedOnUse(tradeData.secondBuyItem().get());
                            }
                            if(tradeData.sellItem() != null && !tradeData.sellItem().isEmpty()) {
                                player.handleExtraItemsCreatedOnUse(tradeData.sellItem());
                            }
                        }

                        for(ShopkeeperInventoryEntry entry : shopkeeperData.inventory()) {
                            int numItems = entry.getAmount();
                            ItemStack copy = entry.getStack().copy();
                            int maxCount = copy.getCount();
                            while(numItems > maxCount) {
                                ItemStack copy2 = copy.copy();
                                copy2.setCount(maxCount);
                                numItems -= maxCount;
                                player.handleExtraItemsCreatedOnUse(copy2);
                            }
                            if(numItems > 0) {
                                copy.setCount(numItems);
                                player.handleExtraItemsCreatedOnUse(copy);
                            }
                        }

                        Shopkeepers.getData().getShopkeeperData().remove(shopkeeper.getUUID());

                        for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                            ItemStack stack = shopkeeper.getItemBySlot(equipmentSlot);
                            player.handleExtraItemsCreatedOnUse(stack);
                            shopkeeper.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                        }

                        shopkeeper.setPos(shopkeeper.getX(), -1000, shopkeeper.getZ());
                        shopkeeper.kill(player.level());
                    }).open();
                }).build());
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
    public void onManualClose() {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());
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
                } else {
                    TradeData oldTrade = shopkeeperData.trades().get(i);
                    if (!oldTrade.equalsIgnoreUses(newTrade)) {
                        if(oldTrade.equalsIgnoreUsesAndCounts(newTrade)) {
                            newTrade = newTrade.withUses(oldTrade.uses());
                        }
                        shopkeeperData.trades().set(i, newTrade);
                        changed = true;
                    }
                }
            }
            else if (shopkeeperData.trades().size() > i && shopkeeperData.trades().get(i) != null) {
                shopkeeperData.trades().set(i, null);
                changed = true;
            }
        }
        if(changed) {
            shopkeeperData.trades().removeIf(Objects::isNull);
            Shopkeepers.getData().setDirty();
        }
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUUID(), player.getUUID());
    }

    protected TradeData validateTradeOrGiveBack(int i) {
        boolean firstBuyItemPresent = !tradeItems.getItem(i).isEmpty();
        boolean secondBuyItemPresent = !tradeItems.getItem(i + 9).isEmpty();
        boolean sellItemPresent = !tradeItems.getItem(i + 18).isEmpty();
        if (firstBuyItemPresent && sellItemPresent) {
            ItemStack firstBuyItem = tradeItems.getItem(i).copy();
            Optional<ItemStack> secondBuyItem =
                    secondBuyItemPresent ? Optional.of(tradeItems.getItem(i + 9).copy()) : Optional.empty();
            ItemStack sellItem = tradeItems.getItem(i + 18).copy();
            return new TradeData(firstBuyItem, secondBuyItem, sellItem, new HashMap<>());
        }
        else {
            if(firstBuyItemPresent) {
                player.handleExtraItemsCreatedOnUse(tradeItems.getItem(i));
            }
            if(secondBuyItemPresent) {
                player.handleExtraItemsCreatedOnUse(tradeItems.getItem(i + 9));
            }
            if(sellItemPresent) {
                player.handleExtraItemsCreatedOnUse(tradeItems.getItem(i + 18));
            }
            return null;
        }
    }

}
