package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public abstract class LockingMerchantGui extends MerchantGui {
    public final LivingEntity shopkeeper;

    public LockingMerchantGui(ServerPlayer player, boolean manipulatePlayerSlots, LivingEntity shopkeeper) {
        super(player, manipulatePlayerSlots);
        this.shopkeeper = shopkeeper;
    }

    @Override
    protected boolean sendGui() {
        if(GuiUtils.ensureInteractionLock(player, shopkeeper)) {
            return super.sendGui();
        }
        else {
            this.close();
            return false;
        }
    }

    @Override
    public void onManualClose() {
        super.onManualClose();
        Shopkeepers.LOGGER.info("onManualClose called");
        onClose();
    }

    @Override
    public void close(boolean skipSync) {
        super.close(skipSync);
        Shopkeepers.LOGGER.info("close called");
        onClose();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        Shopkeepers.LOGGER.info("onRemoved called");
        onClose();
    }

    public void onClose() {
        Shopkeepers.LOGGER.info("lock dropped");
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUUID(), player.getUUID());
    }
}
