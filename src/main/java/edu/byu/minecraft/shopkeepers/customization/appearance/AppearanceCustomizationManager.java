package edu.byu.minecraft.shopkeepers.customization.appearance;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import java.util.ArrayList;
import java.util.List;

public class AppearanceCustomizationManager {

    @SuppressWarnings({"unchecked", "rawtypes"}) /* I know this is annoying, but this is the best way I figured out
    how to get the generics to work to make the customizations extendible */
    public static <E extends Entity> List<AppearanceCustomization<E>> getAppearanceOptions(E entity,
                                                                                           ServerPlayer player,
                                                                                           SimpleGui currentGui) {

        //specific types
        List customization = switch (entity) {
            case Armadillo ae -> ArmadilloCustomizations.getArmadilloCustomizations(ae);
            case ArmorStand ae -> ArmorStandCustomizations.getArmorStandCustomizations(ae);
            case Axolotl ae -> AxolotlCustomizations.getAxolotlCustomizations(ae);
            case Bat be -> BatCustomizations.getBatCustomizations(be);
            case Bee be -> BeeCustomizations.getBeeCustomizations(be);
            case Bogged be -> BoggedCustomizations.getBoggedCustomizations(be);
            case Camel ce -> CamelCustomizations.getCamelCustomizations(ce);
            case Cat ce -> CatCustomizations.getCatCustomizations(ce);
            case Chicken ce -> ChickenCustomizations.getChickenCustomizations(ce);
            case Creeper ce -> CreeperCustomizations.getCreeperCustomizations(ce);
            case CopperGolem cge -> CopperGolemCustomizations.getCopperGolemCustomizations(cge);
            case Cow ce -> CowCustomizations.getCowCustomizations(ce);
            case Frog fe -> FrogCustomizations.getFrogCustomizations(fe);
            case Fox fe -> FoxCustomizations.getFoxCustomizations(fe);
            case GlowSquid gse -> GlowSquidCustomizations.getGlowSquidCustomizations(gse);
            case Goat ge -> GoatCustomizations.getGoatCustomizations(ge);
            case Horse he -> HorseCustomizations.getHorseCustomizations(he);
            case IronGolem ie -> IronGolemCustomizations.getIronGolemCustomizations(ie);
            case Llama le -> LlamaCustomizations.getLlamaCustomizations(le); //also covers trader llama as TraderLlamaEntity extends LlamaEntity
            case Mannequin me -> MannequinCustomizations.getMannequinCustomizations(me, player, currentGui);
            case MushroomCow me -> MooshroomCustomizations.getMooshroomCustomizations(me);
            case Panda pe -> PandaCustomizations.getPandaCustomizations(pe);
            case Parrot pe -> ParrotCustomizations.getParrotCustomizations(pe);
            case Pig pe -> PigCustomizations.getPigCustomizations(pe);
            case Pufferfish pe -> PufferfishCustomizations.getPufferfishCustomizations(pe);
            case Rabbit re -> RabbitCustomizations.getRabbitCustomizations(re);
            case Salmon se -> SalmonCustomizations.getSalmonCustomizations(se);
            case Sheep se -> SheepCustomizations.getSheepCustomizations(se);
            case Shulker se -> ShulkerCustomizations.getShulkerCustomizations(se);
            case Slime se -> SlimeCustomizations.getSlimeCustomizations(se); //also covers Magma Cube
            case SnowGolem sge -> SnowGolemCustomizations.getSnowGolemCustomizations(sge);
            case TropicalFish tfe -> TropicalFishCustomizations.getTropicalFishCustomizations(tfe);
            case Villager ve -> VillagerCustomizations.getVillagerCustomizations(ve);
            case Wolf we -> WolfCustomizations.getWolfCustomizations(we);
            case ZombieVillager zve -> VillagerCustomizations.getVillagerCustomizations(zve);

            default -> new ArrayList<>();
        };

        //generic types
        if(customization.isEmpty()) {
            customization = switch (entity) {
                case TamableAnimal te -> TameableMobCustomizations.getTameableCustomizations(te);
                case AbstractChestedHorse abe -> AbstractDonkeyCustomizations.getAbstractDonkeyCustomizations(abe);
                default -> customization;
            };
        }

        if(entity instanceof Mob mob && mobCanBeBaby(mob)) {
            customization.addFirst(new BabyMobCustomization<>(mob.isBaby()));
        }

        return customization;
    }

    private static boolean mobCanBeBaby(Mob mob) {
        if (mob.isBaby()) {
            return true;
        } else if (mob instanceof WanderingTrader) {
            return false;
        } else {
            mob.setBaby(true);
            boolean baby = mob.isBaby();
            mob.setBaby(false);
            return baby;
        }
    }
}
