package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public class MerchantInventoryGui extends SimpleGui {
    private static final Style INVENTORY_INFO_STYLE;

    static {
        INVENTORY_INFO_STYLE = Style.EMPTY
                .withColor(DyeColor.ORANGE.getTextColor())
                .withItalic(true)
                .withUnderlined(true);
    }

    private final LivingEntity shopkeeper;
    private final SimpleContainer inventoryPage;

    public MerchantInventoryGui(ServerPlayer player, LivingEntity shopkeeper) {
        super(MenuType.GENERIC_9x6, player, false);
        this.shopkeeper = shopkeeper;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());

        int starterSize = shopkeeperData.inventory().size();
        inventoryPage = new SimpleContainer(54 - starterSize);

        for(int i = 0; i < starterSize; i++) {
            setupSlot(i, shopkeeperData.inventory().get(i).getStack(), shopkeeperData);
        }

        for(int i = starterSize; i < 54; i++) {
            this.setSlot(i, new Slot(inventoryPage, i - starterSize, 0, 0));
        }
        setTitle(Component.nullToEmpty(shopkeeper.getName().getString() + " Inventory"));
    }

    private void setupSlot(int i, ItemStack stack, ShopkeeperData shopkeeperData) {
        Integer amount = shopkeeperData.inventoryGet(stack);
        if(amount == null || amount <= 0) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }
        else {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            setSlot(i, GuiElementBuilder.from(copy)
                    .setName(Component.nullToEmpty(String.format("(%d) %s", amount,
                            stack.getHoverName().getString())).toFlatList(stack.getHoverName().getStyle()).getFirst())
                    .setLore(makeLore(copy))
                    .setCallback((clickType) -> {
                        if(clickType == ClickType.MOUSE_LEFT_SHIFT || clickType == ClickType.MOUSE_RIGHT_SHIFT) {
                            boolean hasMore = true;
                            while(hasMore && player.getInventory().getFreeSlot() != -1) {
                                hasMore = giveStack(shopkeeperData, stack, false);
                            }
                        }
                        else {
                            giveStack(shopkeeperData, stack, clickType == ClickType.MOUSE_RIGHT);
                        }

                        setupSlot(i, stack, shopkeeperData);
                        Shopkeepers.getData().setDirty();
                    }));
        }
    }

    private List<Component> makeLore(ItemStack stack) {
        ItemLore loreComponent = stack.getComponents().get(DataComponents.LORE);
        List<Component> lore = new ArrayList<>(loreComponent == null ? List.of() : loreComponent.styledLines());
        lore.add(Component.empty());
        lore.addAll(Component.nullToEmpty("Right Click to receive one").toFlatList(INVENTORY_INFO_STYLE));
        lore.addAll(Component.nullToEmpty("Left Click to receive one stack").toFlatList(INVENTORY_INFO_STYLE));
        lore.addAll(Component.nullToEmpty("Shift Click to fill inventory").toFlatList(INVENTORY_INFO_STYLE));
        lore.add(Component.empty());
        return lore;
    }

    private boolean giveStack(ShopkeeperData shopkeeperData, ItemStack stack, boolean maxOne) {
        int maxSize = maxOne ? 1 : stack.getMaxStackSize();
        Integer amountHeld = shopkeeperData.inventoryGet(stack);
        if (amountHeld == null || amountHeld <= 0) {
            return false;
        }
        int giveSize = Math.min(maxSize, amountHeld);
        ItemStack copy = stack.copy();
        copy.setCount(giveSize);
        player.handleExtraItemsCreatedOnUse(copy);
        shopkeeperData.removeItems(stack, giveSize); //this should subtract the amount from the inventoryEntry we currently have
        return amountHeld > giveSize;
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

    private void save() {
        if (inventoryPage.isEmpty()) return;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUUID());

        for(ItemStack inventoryItemStack : inventoryPage) {
            if(!inventoryItemStack.isEmpty()) {
                shopkeeperData.addItems(inventoryItemStack, inventoryItemStack.getCount());
            }
        }
        Shopkeepers.getData().setDirty();
    }

    @Override
    public void onManualClose() {
        save();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUUID(), player.getUUID());
        save();
    }
}
