package edu.byu.minecraft.shopkeepers.gui;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Items;

public class GuiUtils {

    static final GuiElement EMPTY_SLOT = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).hideTooltip().hideDefaultTooltip().build();
}
