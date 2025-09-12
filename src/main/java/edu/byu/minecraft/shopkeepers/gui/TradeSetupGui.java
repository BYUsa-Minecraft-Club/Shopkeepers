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
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.scanner.NbtCollector;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;
import java.util.function.Predicate;

public abstract class TradeSetupGui extends SimpleGui {

    protected final MobEntity shopkeeper;
    protected final SimpleInventory tradeItems;
    protected final int maxTrades;

    public TradeSetupGui(ScreenHandlerType<?> type, ServerPlayerEntity player, MobEntity shopkeeper, int maxTrades) {
        super(type, player, false);
        this.shopkeeper = shopkeeper;
        this.maxTrades = maxTrades;
        tradeItems = new SimpleInventory(((int) Math.ceil(maxTrades / 9.0)) * 27);
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        List<TradeData> trades = shopkeeperData.trades();
        for (int i = 0; i < trades.size(); i++) {
            int page = i / 9;
            int indexOnPage = i % 9;
            int index = (27 * page) + indexOnPage;
            TradeData tradeOffer = trades.get(i);
            tradeItems.setStack(index, tradeOffer.firstBuyItem().copy());
            if (tradeOffer.secondBuyItem().isPresent()) {
                tradeItems.setStack(index + 9, tradeOffer.secondBuyItem().get().copy());
            }
            tradeItems.setStack(index + 18, tradeOffer.sellItem().copy());
        }

        setupSlots();
    }

    protected abstract void setupSlots();

    protected void showUses(int slot, int trade, boolean showPlayerNames) {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        if(shopkeeperData.trades().size() > trade && shopkeeperData.trades().get(trade) != null) {
            TradeData tradeData = shopkeeperData.trades().get(trade);

            int totalUses = tradeData.uses().values().stream().mapToInt(v -> v).sum();
            var guiBuilder = new GuiElementBuilder(totalUses == 0 ? Items.BOOK : Items.WRITTEN_BOOK)
                    .setItemName(Text.of(String.format("Trade used %d times", totalUses)));

            if(showPlayerNames) {
                guiBuilder.setLore(tradeData.uses().entrySet().stream()
                        .map(entry -> Map.entry(Shopkeepers.getData().getPlayers().get(entry.getKey()),
                                entry.getValue())).sorted((o1, o2) -> {
                            if (Objects.equals(o1.getValue(), o2.getValue())) return o1.getKey().compareTo(o2.getKey());
                            return o2.getValue().compareTo(o1.getValue());
                        }).map(entry -> Text.of(String.format("%s: %d", entry.getKey(), entry.getValue())))
                        .toList());
            }

            setSlot(slot, guiBuilder.build());
        } else {
            setSlot(slot, new GuiElementBuilder(Items.PAPER).setItemName(Text.of("Create trade to track uses")));
        }
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

    //returns whether there's something important now in the slot
    protected boolean appearanceOptions(int slot) {
        List<AppearanceCustomization<Entity>> appearanceOptions =
                AppearanceCustomizationManager.getAppearanceOptions(shopkeeper);
        if (appearanceOptions.isEmpty()) {
            setSlot(slot, GuiUtils.EMPTY_SLOT);
            return false;
        } else if (appearanceOptions.size() > 1) {
            SpawnEggItem egg = SpawnEggItem.forEntity(shopkeeper.getType());
            setSlot(slot, new GuiElementBuilder(egg != null ? egg : Items.LIME_DYE)
                    .setItemName(Text.of(String.format("Edit %s Appearance Options",
                            CustomizationUtils.capitalize(shopkeeper.getType().getName().getString()))))
                    .setCallback(() -> new MobAppearanceGui<>(player, shopkeeper, appearanceOptions, this).open())
                    .build());
            return true;
        } else {
            MobAppearanceGui.setupSlot(this, shopkeeper, slot, appearanceOptions, 0);
            return true;
        }
    }

    protected void equipmentOptions(int slot) {
        List<EquipmentCustomization<MobEntity>> equipmentOptions =
                EquipmentCustomizationManager.getEquipmentOptions(shopkeeper);
        if(equipmentOptions.isEmpty()) {
            setSlot(slot, GuiUtils.EMPTY_SLOT);
        } else {
            setSlot(slot, new GuiElementBuilder(equipmentOptions.getFirst().getDescriptionItem())
                    .setItemName(Text.of(String.format("Edit %s Equipment Options",
                                    CustomizationUtils.capitalize(shopkeeper.getType().getName().getString()))))
                    .setCallback(() -> new MobEquipmentGui<>(player, shopkeeper, equipmentOptions, this).open())
                    .build());
        }

    }

    protected void openTradeMenu(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BELL).setItemName(Text.of("Open Trade Menu")).setCallback(() -> {
            this.close();
            new OfferGui(player, shopkeeper).open();
        }).build());
    }

    protected void teleportShopkeeper(int slot) {
        setSlot(slot, new GuiElementBuilder(Items.ENDER_PEARL)
                .setName(Text.of("Move Shopkeeper"))
                .setCallback(() -> {
                    if(player.getInventory().containsAny(stack -> {
                        NbtComponent nbt = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
                        return nbt != null && nbt.contains(ShopkeeperMover.SHOPKEEPER_ID_KEY);
                    })) {
                        player.sendMessage(Text.of("You already have a teleport item in your inventory"));
                        return;
                    }

                    NbtCompound nbt = new NbtCompound();
                    nbt.putString(ShopkeeperMover.SHOPKEEPER_ID_KEY, shopkeeper.getUuid().toString());

                    String teleportItemName = "Move " + (shopkeeper.hasCustomName() ?
                            Objects.requireNonNull(shopkeeper.getCustomName()).getString() :
                            ("your " + shopkeeper.getName().getString().toLowerCase() + " shopkeeper"));

                    ItemStack teleportItemStack = new ItemStack(Items.ENDER_PEARL.getRegistryEntry(), 1,
                            ComponentChanges.builder()
                                    .add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt))
                                    .add(DataComponentTypes.CUSTOM_NAME, Text.of(teleportItemName))
                                    .add(DataComponentTypes.LORE, new LoreComponent(List.of(Text.empty(),
                                            Text.of("Use this item to move"), Text.of(teleportItemName.substring(5)),
                                            Text.of("to your current location"), Text.empty())))
                                    .add(DataComponentTypes.MAX_STACK_SIZE, 1)
                                    .build());

                    player.giveOrDropStack(teleportItemStack);

                    ShopkeeperMover.addToShopkeeperCache(shopkeeper);
                }));
    }

    protected void disbandShopkeeper(int slot) {
        this.setSlot(slot,
                new GuiElementBuilder(Items.BARRIER).setItemName(Text.of("Disband Shopkeeper")).setCallback(() -> {
                    new ConfirmationGui(player, "disband shopkeeper", this, () -> {
                        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

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

                        for(ShopkeeperInventoryEntry entry : shopkeeperData.inventory()) {
                            int numItems = entry.getAmount();
                            ItemStack copy = entry.getStack().copy();
                            int maxCount = copy.getCount();
                            while(numItems > maxCount) {
                                ItemStack copy2 = copy.copy();
                                copy2.setCount(maxCount);
                                numItems -= maxCount;
                                player.giveOrDropStack(copy2);
                            }
                            if(numItems > 0) {
                                copy.setCount(numItems);
                                player.giveOrDropStack(copy);
                            }
                        }

                        Shopkeepers.getData().getShopkeeperData().remove(shopkeeper.getUuid());

                        for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                            ItemStack stack = shopkeeper.getEquippedStack(equipmentSlot);
                            player.giveOrDropStack(stack);
                            shopkeeper.equipStack(equipmentSlot, ItemStack.EMPTY);
                        }

                        shopkeeper.setPosition(shopkeeper.getX(), -1000, shopkeeper.getZ());
                        shopkeeper.kill(player.getWorld());
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
    public void onClose() {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
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
            Shopkeepers.getData().markDirty();
        }
    }

    @Override
    public void onScreenHandlerClosed() {
        super.onScreenHandlerClosed();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid(), player.getUuid());
    }

    protected TradeData validateTradeOrGiveBack(int i) {
        boolean firstBuyItemPresent = !tradeItems.getStack(i).isEmpty();
        boolean secondBuyItemPresent = !tradeItems.getStack(i + 9).isEmpty();
        boolean sellItemPresent = !tradeItems.getStack(i + 18).isEmpty();
        if (firstBuyItemPresent && sellItemPresent) {
            ItemStack firstBuyItem = tradeItems.getStack(i).copy();
            Optional<ItemStack> secondBuyItem =
                    secondBuyItemPresent ? Optional.of(tradeItems.getStack(i + 9).copy()) : Optional.empty();
            ItemStack sellItem = tradeItems.getStack(i + 18).copy();
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
