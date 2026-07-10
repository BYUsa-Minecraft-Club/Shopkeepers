package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;

public class ConfirmationGui extends SimpleGui {

    public ConfirmationGui(ServerPlayer player, String action, SimpleGui parent, Runnable onConfirm) {
        super(MenuType.GENERIC_9x3, player, false);

        this.setTitle(Component.nullToEmpty("Are you sure?"));

        GuiElement blank = new GuiElementBuilder(Items.STAINED_GLASS_PANE.black())
                .setName(Component.nullToEmpty(String.format("Are you sure you want to %s? This cannot be undone.", action))).build();
        for(int i = 0; i < 27; i++) {
            if (i == 12 || i == 14) continue;
            this.setSlot(i, blank);
        }

        this.setSlot(12, new GuiElementBuilder(Items.STAINED_GLASS_PANE.lime())
                .setName(Component.nullToEmpty(String.format("Confirm %s. (Caution: This cannot be undone)", action)))
                .setCallback(() -> {
                    onConfirm.run();
                    this.close();
                }));

        this.setSlot(14, new GuiElementBuilder(Items.STAINED_GLASS_PANE.red())
                .setName(Component.nullToEmpty("Cancel"))
                .setCallback(() -> {
                    this.close();
                    parent.open();
                }));
    }
}
