package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.FoxEntityVariationSetter;
import edu.byu.minecraft.shopkeepers.mixin.invoker.LlamaEntityVariantSetter;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class CustomizationManager {

    @SuppressWarnings("unchecked") //I know this is annoying, but this is the best way I figured out how to get the
                                   //generics to work to make the customizations extendible
    public static <E extends Entity> CustomizationButtonOptions<E> getCustomizationButtonOptions(E entity,
                                                                                                 ServerPlayerEntity player,
                                                                                                 SimpleGui guiParent) {
        List<? extends ShopkeeperCustomization<? extends Entity>> customization = switch (entity) {
            //specific types
            case ArmadilloEntity ae -> ArmadilloCustomizations.getArmadilloCustomizations(ae);
            case AxolotlEntity ae -> AxolotlCustomizations.getAxolotlCustomizations(ae);
            case CatEntity ce -> CatCustomizations.getCatCustomizations(ce);
            case ChickenEntity ce -> ChickenCustomizations.getChickenCustomizations(ce);
            case CowEntity ce -> CowCustomizations.getCowCustomizations(ce);
            case FrogEntity fe -> FrogCustomizations.getFrogCustomizations(fe);
            case FoxEntity fe -> FoxCustomizations.getFoxCustomizations(fe);
            case HorseEntity he -> HorseCustomizations.getHorseCustomizations(he);
            case LlamaEntity le -> LlamaCustomizations.getLlamaCustomizations(le); //also covers trader llama as TraderLlamaEntity extends LlamaEntity
            case MooshroomEntity me -> MooshroomCustomizations.getMooshroomCustomizations(me);
            case ParrotEntity pe -> ParrotCustomizations.getParrotCustomizations(pe);
            case PigEntity pe -> PigCustomizations.getPigCustomizations(pe);
            case RabbitEntity re -> RabbitCustomizations.getRabbitCustomizations(re);
            case VillagerEntity ve -> VillagerCustomizations.getVillagerCustomizations(ve);
            case WolfEntity we -> WolfCustomizations.getWolfCustomizations(we);
            case ZombieVillagerEntity ze -> VillagerCustomizations.getVillagerCustomizations(ze);

            //generic types
            case TameableEntity te -> TameableMobCustomizations.getTameableCustomizations(te);
            case AbstractDonkeyEntity abe -> AbstractDonkeyCustomizations.getAbstractDonkeyCustomizations(abe);
            default -> null;
        };

        if (customization == null) return null;

        return new CustomizationButtonOptions<>(SpawnEggItem.forEntity(entity.getType()),
                (List<ShopkeeperCustomization<E>>) customization,
                CustomizationUtils.capitalize(entity.getType().getName().getString()));
    }
}
