package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.shopkeepers.customization.equipment.EquipmentCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MobEquipmentGui<E extends Entity> extends SimpleGui {

    private final List<EquipmentCustomization<E>> equipmentCustomizations;

    private final SimpleContainer inventory;

    private final E shopkeeper;

    public MobEquipmentGui(ServerPlayer player, E shopkeeper,
                           List<EquipmentCustomization<E>> equipmentCustomizations,
                           SimpleGui parent) {
        super(MenuType.GENERIC_9x3, player, false);
        this.shopkeeper = shopkeeper;
        this.inventory = new SimpleContainer(equipmentCustomizations.size());
        this.equipmentCustomizations = equipmentCustomizations;

        for(int i = 0; i < equipmentCustomizations.size(); i++) {
            EquipmentCustomization<E> equipment = equipmentCustomizations.get(i);
            inventory.setItem(i, equipment.getInitalStack(shopkeeper).copy());
            setSlot(i, new Slot(inventory, i, 0, 0));
            setSlot(i + 9, new GuiElementBuilder(equipment.getDescriptionItem())
                    .setItemName(Component.nullToEmpty(String.format("↑ Place %s in slot above this ↑",
                            equipment.equipmentSlotDescription().toLowerCase()))).build());
        }

        for(int i = equipmentCustomizations.size(); i < 9; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
            setSlot(i + 9, GuiUtils.EMPTY_SLOT);
        }

        for(int i = 18; i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setItemName(Component.nullToEmpty("Close")).setCallback(() -> {
            this.close();
            parent.open();
        }).build());

    }

    @Override
    public void onManualClose() {
        for(int i = 0; i < equipmentCustomizations.size(); i++) {
            EquipmentCustomization<E> equipment = equipmentCustomizations.get(i);
            ItemStack itemStack = inventory.getItem(i);
            String validation = equipment.validate(shopkeeper, itemStack);
            if (validation == null) {
                equipment.updateEquipment(shopkeeper, itemStack);
            } else {
                player.sendSystemMessage(Component.nullToEmpty(validation));
                player.handleExtraItemsCreatedOnUse(itemStack);
                equipment.updateEquipment(shopkeeper, ItemStack.EMPTY);
            }
        }
    }
}
