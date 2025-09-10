package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.shopkeepers.customization.equipment.EquipmentCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HeldItemGui<E extends Entity> extends SimpleGui {

    private final EquipmentCustomization<E> heldItemCustomization;

    private final SimpleInventory inventory;

    private final E shopkeeper;

    private final ItemStack initialStack;

    public HeldItemGui(ServerPlayerEntity player, E shopkeeper, EquipmentCustomization<E> heldItemCustomization,
            SimpleGui parent) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.shopkeeper = shopkeeper;
        this.initialStack = heldItemCustomization.getInitalStack(shopkeeper);
        inventory = new SimpleInventory(1);
        inventory.setStack(0, initialStack.copy());
        this.heldItemCustomization = heldItemCustomization;
        setSlotRedirect(13, new Slot(inventory, 0, 0, 0));
        setSlot(22, new GuiElementBuilder(heldItemCustomization.getDescriptionItem())
                .setItemName(Text.of(String.format("↑ Place %s in slot above this ↑",
                        heldItemCustomization.equipmentSlotDescription().toLowerCase()))).build());
    }

    @Override
    public void onClose() {
        ItemStack itemStack = inventory.getStack(0);
        String validation = heldItemCustomization.validate(shopkeeper, itemStack);
        if (validation == null) {
            heldItemCustomization.updateEquipment(shopkeeper, itemStack);
        } else {
            player.sendMessage(Text.of(validation));
            player.giveOrDropStack(itemStack);
            heldItemCustomization.updateEquipment(shopkeeper, ItemStack.EMPTY);
        }

        super.onClose();
    }
}
