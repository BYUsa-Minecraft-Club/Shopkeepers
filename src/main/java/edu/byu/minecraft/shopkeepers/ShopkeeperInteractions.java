package edu.byu.minecraft.shopkeepers;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.data.ShopkeeperData;
import edu.byu.minecraft.shopkeepers.gui.AdminShopTradeSetupGui;
import edu.byu.minecraft.shopkeepers.gui.OfferGui;
import edu.byu.minecraft.shopkeepers.gui.PlayerShopTradeSetupGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class ShopkeeperInteractions {
    public static void warnDeathPrevented(LivingEntity target) {
        List<UUID> owners = Shopkeepers.getData().getShopkeeperData().get(target.getUuid()).owners();
        String warning = String.format("Shopkeeper entity %s owned by %s was attempted to be killed (likely by a command). " +
                        "Please disband the shop first. Location: X: %.0f, Y:%.0f, Z:%.0f",
                target.getName().getString(),
                owners.isEmpty() ? "admins" :
                        owners.stream().map(uuid -> Shopkeepers.getData().getPlayers().get(uuid))
                                .sorted(String::compareToIgnoreCase).toList(),
                target.getX(), target.getY(), target.getZ());

        if(target.getEntityWorld().getServer() != null) {
            for (String opName : target.getEntityWorld().getServer().getPlayerManager().getOpNames()) {
                ServerPlayerEntity op = target.getEntityWorld().getServer().getPlayerManager().getPlayer(opName);
                if (op != null) {
                    op.sendMessage(Text.of(warning));
                }
            }
        }

        Shopkeepers.LOGGER.warn(warning);
    }

    //returns if a gui was opened
    public static boolean openGui(Entity entity, ServerPlayerEntity player) {
        ShopkeeperData shopkeeperData = Shopkeepers.getData().getShopkeeperData().get(entity.getUuid());
        if (shopkeeperData != null) {
            if(shopkeeperData.isAdmin() && player.getEntityWorld().getServer().getPlayerManager().isOperator(player.getPlayerConfigEntry())) {
                if(shopkeeperData.trades().isEmpty()) {
                    new AdminShopTradeSetupGui(player, (LivingEntity) entity).open();
                } else {
                    new OfferGui(player, (LivingEntity) entity).open();
                }
            } else if (shopkeeperData.owners().contains(player.getUuid())) {
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
