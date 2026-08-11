package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuType;


public abstract class LockingGui extends SimpleGui {
    public final LivingEntity shopkeeper;
    private boolean open = false;

    public LockingGui(MenuType<?> type, ServerPlayer player, boolean manipulatePlayerSlots, LivingEntity shopkeeper) {
        super(type, player, manipulatePlayerSlots);
        this.shopkeeper = shopkeeper;
    }

    @Override
    protected boolean sendGui() {
        if(GuiUtils.ensureInteractionLock(player, shopkeeper)) {
            this.open = true;
            return super.sendGui();
        }
        else {
            this.close();
            return false;
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (this.open) {
            onClose();
        }
    }

    public void onClose() {
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUUID(), player.getUUID());
    }
}
