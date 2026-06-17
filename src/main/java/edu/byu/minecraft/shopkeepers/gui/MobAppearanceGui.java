package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.shopkeepers.customization.appearance.AppearanceCustomization;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;

public class MobAppearanceGui<E extends Entity> extends SimpleGui {

    public MobAppearanceGui(ServerPlayer player, E shopkeeper,
                            List<AppearanceCustomization<E>> customizations, SimpleGui parent) {
        super(MenuType.GENERIC_9x3, player, false);
        customizations = new ArrayList<>(customizations);

        for (int i = 0; i < customizations.size(); i++) {
            setupSlot(this, shopkeeper, i, customizations, i);
        }

        for (int i = customizations.size(); i < 26; i++) {
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(26, new GuiElementBuilder(Items.BARRIER).setItemName(Component.nullToEmpty("Close")).setCallback(() -> {
            this.close();
            parent.open();
        }).build());
    }

    static <E extends Entity> void setupSlot(SimpleGui gui, E shopkeeper, int slot,
                                             List<AppearanceCustomization<E>> customizations, int index) {
        AppearanceCustomization<E> customization = customizations.get(index);
        gui.setSlot(slot, new GuiElementBuilder(customization.getCurrentRepresentationItem()).setItemName(
                Component.nullToEmpty("Cycle " + customization.customizationDescription())).setLore(
                List.of(Component.nullToEmpty("Current " + customization.customizationDescription() + ": "),
                        Component.nullToEmpty(customization.currentDescription()))).setCallback(() -> {
            customizations.set(index, customization.setNext(shopkeeper));
            setupSlot(gui, shopkeeper, slot, customizations, index);
        }));
    }
}
