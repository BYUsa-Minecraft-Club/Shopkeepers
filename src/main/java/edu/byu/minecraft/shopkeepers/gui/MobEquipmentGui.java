package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.shopkeepers.customization.equipment.EquipmentCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class MobEquipmentGui<E extends Entity> extends SimpleGui {

    private final List<EquipmentCustomization<E>> equipmentCustomizations;

    private final SimpleInventory inventory;

    private final E shopkeeper;

    public MobEquipmentGui(ServerPlayerEntity player, E shopkeeper,
                           List<EquipmentCustomization<E>> equipmentCustomizations,
                           SimpleGui parent) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.shopkeeper = shopkeeper;
        this.inventory = new SimpleInventory(equipmentCustomizations.size());
        this.equipmentCustomizations = equipmentCustomizations;

        for(int i = 0; i < equipmentCustomizations.size(); i++) {
            EquipmentCustomization<E> equipment = equipmentCustomizations.get(i);
            inventory.setStack(i, equipment.getInitalStack(shopkeeper).copy());
            setSlotRedirect(i, new Slot(inventory, i, 0, 0));
            setSlot(i + 9, new GuiElementBuilder(equipment.getDescriptionItem())
                    .setItemName(Text.of(String.format("↑ Place %s in slot above this ↑",
                            equipment.equipmentSlotDescription().toLowerCase()))).build());
        }

        for(int i = equipmentCustomizations.size(); i < 9; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
            setSlot(i + 9, GuiUtils.EMPTY_SLOT);
        }

        for(int i = 18; i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setItemName(Text.of("Close")).setCallback(() -> {
            this.close();
            parent.open();
        }).build());

    }

    @Override
    public void onClose() {
        for(int i = 0; i < equipmentCustomizations.size(); i++) {
            EquipmentCustomization<E> equipment = equipmentCustomizations.get(i);
            ItemStack itemStack = inventory.getStack(i);
            String validation = equipment.validate(shopkeeper, itemStack);
            if (validation == null) {
                equipment.updateEquipment(shopkeeper, itemStack);
            } else {
                player.sendMessage(Text.of(validation));
                player.giveOrDropStack(itemStack);
                equipment.updateEquipment(shopkeeper, ItemStack.EMPTY);
            }
        }

        super.onClose();
    }
}
