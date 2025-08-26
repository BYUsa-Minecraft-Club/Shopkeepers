package edu.byu.minecraft.shopkeepers.gui;

import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.customization.VillagerProfessions;
import edu.byu.minecraft.shopkeepers.customization.VillagerTypes;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class VillagerSettingsGui extends SimpleGui {

    private final VillagerEntity shopkeeper;

    public VillagerSettingsGui(ServerPlayerEntity player, VillagerEntity shopkeeper, SimpleGui parent) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.shopkeeper = shopkeeper;


        for (int i = 1; i < 27; i++) {
            if (i == 11 || i == 13 || i == 15) continue;
            setSlot(i, GuiUtils.EMPTY_SLOT);
        }

        setSlot(0, new GuiElementBuilder(Items.BARRIER).setItemName(Text.of("Close")).setCallback(() -> {
            this.close();
            parent.open();
            Shopkeepers.getInteractionLocks().tryAcquireLock(shopkeeper.getUuid(), player.getUuid());
        }).build());

        villagerProfessionCycler();
        villagerTypeCycler();
        villagerLevelCycler();
    }

    protected void villagerLevelCycler() {
        int current = shopkeeper.getVillagerData().level();
        setSlot(15, new GuiElementBuilder(switch (current) {
            case 1 -> Items.COBBLESTONE;
            case 2 -> Items.IRON_INGOT;
            case 3 -> Items.GOLD_INGOT;
            case 4 -> Items.EMERALD;
            case 5 -> Items.DIAMOND;
            default -> throw new IllegalStateException("Unexpected value: " + current);
        }).setItemName(Text.of("Cycle Villager Level"))
                .setLore(List.of(Text.of("Current Level: "), Text.of(switch (current) {
                    case 1 -> "Novice";
                    case 2 -> "Apprentice";
                    case 3 -> "Journeyman";
                    case 4 -> "Expert";
                    case 5 -> "Master";
                    default -> throw new IllegalStateException("Unexpected value: " + current);
                }))).setCallback(() -> {
                    shopkeeper.setVillagerData(shopkeeper.getVillagerData().withLevel((current % 5) + 1));
                    villagerLevelCycler();
                }).build());
    }

    private void villagerProfessionCycler() {
        VillagerProfessions current =
                VillagerProfessions.getVillagerProfession(shopkeeper.getVillagerData().profession().getKey().get());
        setSlot(11,
                new GuiElementBuilder(current.getRepresentationItem()).setItemName(Text.of("Cycle Villager Profession"))
                        .setLore(List.of(Text.of("Current Profssion:"),
                                Text.of(current.getProfession().getValue().getPath()))).setCallback(() -> {
                            shopkeeper.setVillagerData(shopkeeper.getVillagerData()
                                    .withProfession(player.getRegistryManager(),
                                            current.getNextInCycle().getProfession()));
                            villagerProfessionCycler();
                        }).build());
    }

    private void villagerTypeCycler() {
        VillagerTypes current = VillagerTypes.getVillagerType(shopkeeper.getVillagerData().type().getKey().get());
        setSlot(13, new GuiElementBuilder(current.getRepresentationItem()).setItemName(Text.of("Cycle Villager Type"))
                .setLore(List.of(Text.of("Current Type:"), Text.of(current.getType().getValue().getPath())))
                .setCallback(() -> {
                    shopkeeper.setVillagerData(shopkeeper.getVillagerData()
                            .withType(player.getRegistryManager(), current.getNextInCycle().getType()));
                    villagerTypeCycler();
                }).build());
    }

    @Override
    public void onClose() {
        super.onClose();
        Shopkeepers.getInteractionLocks().releaseLock(shopkeeper.getUuid());
    }
}
