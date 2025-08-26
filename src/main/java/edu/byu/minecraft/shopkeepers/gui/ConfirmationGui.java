package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ConfirmationGui extends SimpleGui {

    public ConfirmationGui(ServerPlayerEntity player, String action, SimpleGui parent, Runnable onConfirm) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);

        this.setTitle(Text.of("Are you sure?"));

        GuiElement blank = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE)
                .setName(Text.of(String.format("Are you sure you want to %s? This cannot be undone.", action))).build();
        for(int i = 0; i < 27; i++) {
            if (i == 12 || i == 14) continue;
            this.setSlot(i, blank);
        }

        this.setSlot(12, new GuiElementBuilder(Items.LIME_STAINED_GLASS_PANE)
                .setName(Text.of(String.format("Confirm %s. (Caution: This cannot be undone)", action)))
                .setCallback(() -> {
                    onConfirm.run();
                    this.close();
                }));

        this.setSlot(14, new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
                .setName(Text.of("Cancel"))
                .setCallback(() -> {
                    this.close();
                    parent.open();
                }));
    }
}
