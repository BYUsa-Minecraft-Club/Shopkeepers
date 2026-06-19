package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.AnvilInputGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;

public class TextInputGui extends AnvilInputGui {
    private final String initial;
    private final Consumer<String> callback;
    private final SimpleGui parent;
    public TextInputGui(ServerPlayer player, String initial, Consumer<String> callback, SimpleGui parent) {
        super(player, false);
        setDefaultInputValue((initial == null) ? "" : initial);
        this.initial = initial;
        this.callback = callback;
        this.parent = parent;
    }

    @Override
    public void onInput(String input) {
        super.onInput(input);
        if (!Objects.equals(input, initial)) {
            this.setSlot(2, new GuiElementBuilder(Items.PAPER)
                    .setItemName(Component.nullToEmpty(input))
                    .setCallback(() -> this.close()));
        }
    }

    @Override
    public void onManualClose() {
        if(!Objects.equals(initial, this.getInput())) {
            callback.accept(this.getInput());
            player.setExperienceLevels(player.experienceLevel); //no idea why it works like this but
            // without this line the experience level drops by one each time and if you set it to
            // player.experienceLevel + 1 it adds by one each time

            if(parent != null) {
                parent.open();
            }
        }
    }
}
