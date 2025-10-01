package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class MerchantInventoryGui extends SimpleGui {
    private static final Style INVENTORY_INFO_STYLE;

    static {
        INVENTORY_INFO_STYLE = Style.EMPTY
                .withColor(DyeColor.ORANGE.getSignColor())
                .withItalic(true)
                .withUnderline(true);
    }

    private final LivingEntity shopkeeper;
    private final SimpleInventory inventoryPage;

    public MerchantInventoryGui(ServerPlayerEntity player, LivingEntity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.shopkeeper = shopkeeper;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

        int starterSize = shopkeeperData.inventory().size();
        inventoryPage = new SimpleInventory(54 - starterSize);

        for(int i = 0; i < starterSize; i++) {
            setupSlot(i, shopkeeperData.inventory().get(i).getStack(), shopkeeperData);
        }

        for(int i = starterSize; i < 54; i++) {
            this.setSlotRedirect(i, new Slot(inventoryPage, i - starterSize, 0, 0));
        }
        setTitle(Text.of(shopkeeper.getName().getString() + " Inventory"));
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
                    .setName(Text.of(String.format("(%d) %s", amount,
                            stack.getName().getString())).getWithStyle(stack.getName().getStyle()).getFirst())
                    .setLore(makeLore(copy))
                    .setCallback((index, clickType, slotActionType) -> {
                        if(clickType == ClickType.MOUSE_LEFT_SHIFT || clickType == ClickType.MOUSE_RIGHT_SHIFT) {
                            boolean hasMore = true;
                            while(hasMore && player.getInventory().getEmptySlot() != -1) {
                                hasMore = giveStack(shopkeeperData, stack, false);
                            }
                        }
                        else {
                            giveStack(shopkeeperData, stack, clickType == ClickType.MOUSE_RIGHT);
                        }

                        setupSlot(i, stack, shopkeeperData);
                        Shopkeepers.getData().markDirty();
                    }));
        }
    }

    private List<Text> makeLore(ItemStack stack) {
        LoreComponent loreComponent = stack.getComponents().get(DataComponentTypes.LORE);
        List<Text> lore = new ArrayList<>(loreComponent == null ? List.of() : loreComponent.styledLines());
        lore.add(Text.empty());
        lore.addAll(Text.of("Right Click to receive one").getWithStyle(INVENTORY_INFO_STYLE));
        lore.addAll(Text.of("Left Click to receive one stack").getWithStyle(INVENTORY_INFO_STYLE));
        lore.addAll(Text.of("Shift Click to fill inventory").getWithStyle(INVENTORY_INFO_STYLE));
        lore.add(Text.empty());
        return lore;
    }

    private boolean giveStack(ShopkeeperData shopkeeperData, ItemStack stack, boolean maxOne) {
        int maxSize = maxOne ? 1 : stack.getMaxCount();
        Integer amountHeld = shopkeeperData.inventoryGet(stack);
        if (amountHeld == null || amountHeld <= 0) {
            return false;
        }
        int giveSize = Math.min(maxSize, amountHeld);
        ItemStack copy = stack.copy();
        copy.setCount(giveSize);
        player.giveOrDropStack(copy);
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

    @Override
    public void onClose() {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

        for(ItemStack inventoryItemStack : inventoryPage) {
            if(!inventoryItemStack.isEmpty()) {
                shopkeeperData.addItems(inventoryItemStack, inventoryItemStack.getCount());
            }
        }
        Shopkeepers.getData().markDirty();
    }

    @Override
    public void onScreenHandlerClosed() {
        super.onScreenHandlerClosed();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid(), player.getUuid());
    }
}
