package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperInventoryEntry;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MerchantInventoryGui extends SimpleGui {

    private final Entity shopkeeper;
    private final SimpleInventory inventoryPage;

    public MerchantInventoryGui(ServerPlayerEntity player, Entity shopkeeper) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.shopkeeper = shopkeeper;
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());
        List<ItemStack> inventoryStacks = shopkeeperData.inventory().stream()
                .mapMulti((BiConsumer<ShopkeeperInventoryEntry, Consumer<ItemStack>>) (entry, consumer) -> {
                    int numItems = entry.getAmount();
                    int maxCount = entry.getStack().getMaxCount();
                    ItemStack copy = entry.getStack().copy();
                    while (numItems > maxCount) {
                        ItemStack newStack = copy.copy();
                        newStack.setCount(maxCount);
                        consumer.accept(newStack);
                        numItems -= maxCount;
                    }
                    if (numItems > 0) {
                        copy.setCount(numItems);
                        consumer.accept(copy);
                    }
                }).toList();
        inventoryPage = new SimpleInventory(Math.max(54, inventoryStacks.size()));
        for(int i = 0; i < inventoryStacks.size(); i++) {
            inventoryPage.setStack(i, inventoryStacks.get(i));
        }
        for(int i = 0; i < 54; i++) {
            this.setSlotRedirect(i, new Slot(inventoryPage, i, 0, 0));
        }
        setTitle(Text.of(shopkeeper.getName().getString() + " Inventory"));
    }

    @Override
    public void onClose() {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(shopkeeper.getUuid());

        shopkeeperData.inventory().clear();
        for(ItemStack inventoryItemStack : inventoryPage) {
            if(!inventoryItemStack.isEmpty()) {
                shopkeeperData.addItems(inventoryItemStack, inventoryItemStack.getCount());
            }
        }
        Shopkeepers.getData().markDirty();

        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid());
    }
}
