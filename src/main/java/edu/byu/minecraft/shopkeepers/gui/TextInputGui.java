package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.AnvilInputGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Consumer;

public class TextInputGui extends AnvilInputGui {
    private final String initial;
    private final Consumer<String> callback;
    private final SimpleGui parent;
    public TextInputGui(ServerPlayerEntity player, String initial, Consumer<String> callback, SimpleGui parent) {
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
                    .setItemName(Text.of(input))
                    .setCallback(() -> this.close()));
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if(!Objects.equals(initial, this.getInput())) {
            callback.accept(this.getInput());
            player.setExperienceLevel(player.experienceLevel); //no idea why it works like this but
            // without this line the experience level drops by one each time and if you set it to
            // player.experienceLevel + 1 it adds by one each time

            if(parent != null) {
                parent.open();
            }
        }
    }
}
