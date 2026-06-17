package edu.byu.minecraft.shopkeepers;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.gui.AdminShopTradeSetupGui;
import edu.byu.minecraft.shopkeepers.gui.OfferGui;
import edu.byu.minecraft.shopkeepers.gui.PlayerShopTradeSetupGui;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ShopkeeperInteractions {
    public static void warnDeathPrevented(LivingEntity target) {
        List<UUID> owners = Shopkeepers.getData().getShopkeeperData().get(target.getUUID()).owners();
        String warning = String.format("Shopkeeper entity %s owned by %s was attempted to be killed (likely by a command). " +
                        "Please disband the shop first. Location: X: %.0f, Y:%.0f, Z:%.0f",
                target.getName().getString(),
                owners.isEmpty() ? "admins" :
                        owners.stream().map(uuid -> Shopkeepers.getData().getPlayers().get(uuid))
                                .sorted(String::compareToIgnoreCase).toList(),
                target.getX(), target.getY(), target.getZ());

        if(target.level().getServer() != null) {
            for (String opName : target.level().getServer().getPlayerList().getOpNames()) {
                ServerPlayer op = target.level().getServer().getPlayerList().getPlayerByName(opName);
                if (op != null) {
                    op.sendSystemMessage(Component.nullToEmpty(warning));
                }
            }
        }

        Shopkeepers.LOGGER.warn(warning);
    }

    //returns if a gui was opened
    public static boolean openGui(Entity entity, ServerPlayer player) {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(entity.getUUID());
        if (shopkeeperData != null) {
            if(shopkeeperData.isAdmin() && Shopkeepers.isAdmin(player)) {
                if(shopkeeperData.trades().isEmpty()) {
                    new AdminShopTradeSetupGui(player, (LivingEntity) entity).open();
                } else {
                    new OfferGui(player, (LivingEntity) entity).open();
                }
            } else if (shopkeeperData.owners().contains(player.getUUID())) {
                new PlayerShopTradeSetupGui(player, (LivingEntity) entity).open();
            } else {
                new OfferGui(player, (LivingEntity) entity).open();
            }
            return true;
        }
        else {
            return false;
        }
    }
}
