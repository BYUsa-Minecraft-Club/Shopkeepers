package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.customization.ShopkeeperCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MobSettingsGui<E extends Entity> extends SimpleGui {

    public MobSettingsGui(ServerPlayerEntity player, E shopkeeper,
                          List<ShopkeeperCustomization<E>> customizations, SimpleGui parent) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        customizations = new ArrayList<>(customizations);

        for (int i = 0; i < customizations.size(); i++) {
            setupSlot(this, shopkeeper, i, customizations, i);
        }

        for (int i = customizations.size(); i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setItemName(Text.of("Close")).setCallback(() -> {
            this.close();
            parent.open();
            Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUuid(), player.getUuid());
        }).build());
    }

    static <E extends Entity> void setupSlot(SimpleGui gui, E shopkeeper, int slot,
                                             List<ShopkeeperCustomization<E>> customizations, int index) {
        ShopkeeperCustomization<E> customization = customizations.get(index);
        gui.setSlot(slot, new GuiElementBuilder(customization.getCurrentRepresentationItem()).setItemName(
                Text.of("Cycle " + customization.customizationDescription())).setLore(
                List.of(Text.of("Current " + customization.customizationDescription() + ": "),
                        Text.of(customization.currentDescription()))).setCallback(() -> {
            customizations.set(index, customization.setNext(shopkeeper));
            setupSlot(gui, shopkeeper, slot, customizations, index);
        }));
    }
}
