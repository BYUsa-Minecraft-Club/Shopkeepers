package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.FoxEntityVariationSetter;
import edu.byu.minecraft.shopkeepers.mixin.invoker.LlamaEntityVariantSetter;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomizationManager {

    @SuppressWarnings({"unchecked", "rawtypes"}) /* I know this is annoying, but this is the best way I figured out
    how to get the generics to work to make the customizations extendible */
    public static <E extends Entity> CustomizationButtonOptions<E> getCustomizationButtonOptions(E entity,
                                                                                                 ServerPlayerEntity player,
                                                                                                 SimpleGui guiParent) {
        HeldItemCustomization heldItemCustomization = switch (entity) {
            case AllayEntity ae -> AllayCustomizations.getHeldItemCustomization(ae);
            case EndermanEntity ee -> EndermanCustomization.getHeldItemCustomization(ee);

            default -> null;
        };

        //specific types
        List customization = switch (entity) {
            case ArmadilloEntity ae -> ArmadilloCustomizations.getArmadilloCustomizations(ae);
            case AxolotlEntity ae -> AxolotlCustomizations.getAxolotlCustomizations(ae);
            case BatEntity be -> BatCustomizations.getBatCustomizations(be);
            case BeeEntity be -> BeeCustomizations.getBeeCustomizations(be);
            case CatEntity ce -> CatCustomizations.getCatCustomizations(ce);
            case ChickenEntity ce -> ChickenCustomizations.getChickenCustomizations(ce);
            case CreeperEntity ce -> CreeperCustomizations.getCreeperCustomizations(ce);
            case CowEntity ce -> CowCustomizations.getCowCustomizations(ce);
            case FrogEntity fe -> FrogCustomizations.getFrogCustomizations(fe);
            case FoxEntity fe -> FoxCustomizations.getFoxCustomizations(fe);
            case GoatEntity ge -> GoatCustomizations.getGoatCustomizations(ge);
            case HorseEntity he -> HorseCustomizations.getHorseCustomizations(he);
            case IronGolemEntity ie -> IronGolemCustomizations.getIronGolemCustomizations(ie);
            case LlamaEntity le -> LlamaCustomizations.getLlamaCustomizations(le); //also covers trader llama as TraderLlamaEntity extends LlamaEntity
            case MooshroomEntity me -> MooshroomCustomizations.getMooshroomCustomizations(me);
            case PandaEntity pe -> PandaCustomizations.getPandaCustomizations(pe);
            case ParrotEntity pe -> ParrotCustomizations.getParrotCustomizations(pe);
            case PigEntity pe -> PigCustomizations.getPigCustomizations(pe);
            case PufferfishEntity pe -> PufferfishCustomizations.getPufferfishCustomizations(pe);
            case RabbitEntity re -> RabbitCustomizations.getRabbitCustomizations(re);
            case SalmonEntity se -> SalmonCustomizations.getSalmonCustomizations(se);
            case SheepEntity se -> SheepCustomizations.getSheepCustomizations(se);
            case ShulkerEntity se -> ShulkerCustomizations.getShulkerCustomizations(se);
            case SlimeEntity se -> SlimeCustomizations.getSlimeCustomizations(se); //also covers Magma Cube
            case SnowGolemEntity sge -> SnowGolemCustomizations.getSnowGolemCustomizations(sge);
            case TropicalFishEntity tfe -> TropicalFishCustomizations.getTropicalFishCustomizations(tfe);
            case VillagerEntity ve -> VillagerCustomizations.getVillagerCustomizations(ve);
            case WolfEntity we -> WolfCustomizations.getWolfCustomizations(we);
            case ZombieVillagerEntity zve -> VillagerCustomizations.getVillagerCustomizations(zve);

            default -> new ArrayList<>();
        };

        //generic types
        if(customization.isEmpty()) {
            customization = switch (entity) {
                case TameableEntity te -> TameableMobCustomizations.getTameableCustomizations(te);
                case AbstractDonkeyEntity abe -> AbstractDonkeyCustomizations.getAbstractDonkeyCustomizations(abe);
                default -> new ArrayList<>();
            };
        }

        if(entity instanceof MobEntity mob && mobCanBeBaby(mob)) {
            customization.addFirst(new BabyMobCustomization<>(mob.isBaby()));
        }


        if (customization.isEmpty() && heldItemCustomization == null) {
            return null;
        }

        return new CustomizationButtonOptions<>(SpawnEggItem.forEntity(entity.getType()),
                (List<ShopkeeperCustomization<E>>) customization,
                heldItemCustomization,
                CustomizationUtils.capitalize(entity.getType().getName().getString()));
    }

    private static boolean mobCanBeBaby(MobEntity mob) {
        if (mob.isBaby()) {
            return true;
        } else {
            mob.setBaby(true);
            boolean baby = mob.isBaby();
            mob.setBaby(false);
            return baby;
        }
    }
}
