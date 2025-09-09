package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.shopkeepers.customization.appearance.HeldItemCustomization;
import edu.byu.minecraft.shopkeepers.customization.appearance.AppearanceCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class HeldItemGui<E extends Entity> extends MobSettingsGui<E> {

    private final HeldItemCustomization heldItemCustomization;

    private final SimpleInventory inventory;

    public HeldItemGui(ServerPlayerEntity player, E shopkeeper, List<AppearanceCustomization<E>> customizations,
                       HeldItemCustomization heldItemCustomization, SimpleGui parent) {
        super(player, shopkeeper, customizations, parent);
        inventory = new SimpleInventory(1);
        inventory.setStack(0, heldItemCustomization.initalStack().get());
        this.heldItemCustomization = heldItemCustomization;
        setSlotRedirect(13, new Slot(inventory, 0, 0, 0));
        setSlot(22, new GuiElementBuilder(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setItemName(Text.of("↑ Place held item in slot above this ↑")));
    }

    @Override
    public void onClose() {
        ItemStack itemStack = inventory.getStack(0);
        String validation = heldItemCustomization.validator().validate(itemStack);
        if(validation != null) {
            player.sendMessage(Text.of(validation));
            player.giveOrDropStack(itemStack);
        } else if (!ItemStack.areEqual(itemStack, heldItemCustomization.initalStack().get())) {
            heldItemCustomization.onUpdateStack().accept(itemStack);
        }

        super.onClose();
    }
}
