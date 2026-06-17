package edu.byu.minecraft.shopkeepers.customization.appearance;

import com.mojang.authlib.GameProfile;
import edu.byu.minecraft.Shopkeepers;
import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.gui.TextInputGui;
import edu.byu.minecraft.shopkeepers.mixin.invoker.MannequinEntityCustomizationInvoker;
import eu.pb4.sgui.api.gui.SimpleGui;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

public class MannequinCustomizations {
    public static List<AppearanceCustomization<Mannequin>> getMannequinCustomizations(Mannequin entity,
                                                                                            ServerPlayer player,
                                                                                            SimpleGui currentGui) {
        List<AppearanceCustomization<Mannequin>> customizations = new ArrayList<>();
        if (Shopkeepers.isAdmin(player)) {
            customizations.add(new MannequinProfileCustomization(entity, player, currentGui));
        }
        customizations.add(new MannequinPoseCustomization(entity));
        return customizations;
    }

    private record MannequinPoseCustomization(MannequinPose mannequinPose)
            implements AppearanceCustomization<Mannequin> {
        private enum MannequinPose {
            STANDING(Pose.STANDING), CROUCHING(Pose.CROUCHING), SWIMMING(Pose.SWIMMING),
            GLIDING(Pose.FALL_FLYING);

            private final Pose pose;

            private MannequinPose(Pose pose) {
                this.pose = pose;
            }

            private static MannequinPose of(Pose pose) {
                for (MannequinPose mannequinPose : MannequinPose.values()) {
                    if (mannequinPose.pose.equals(pose)) {
                        return mannequinPose;
                    }
                }
                return STANDING;
            }

        }

        private MannequinPoseCustomization(Mannequin mannequin) {
            this(MannequinPose.of(mannequin.getPose()));
        }

        @Override
        public String customizationDescription() {
            return "Pose";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(mannequinPose.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (mannequinPose) {
                case STANDING -> Items.ARMOR_STAND;
                case CROUCHING -> Items.RABBIT_HIDE;
                case SWIMMING -> Items.WATER_BUCKET;
                case GLIDING -> Items.ELYTRA;
            };
        }

        @Override
        public AppearanceCustomization<Mannequin> setNext(Mannequin shopkeeper) {
            MannequinPose next = CustomizationUtils.nextEnum(mannequinPose, MannequinPose.values());
            shopkeeper.setPose(next.pose);
            return new MannequinPoseCustomization(next);
        }
    }

    private record MannequinProfileCustomization(Mannequin mannequin, ServerPlayer player, SimpleGui currentGui)
            implements AppearanceCustomization<Mannequin> {

        @Override
        public String customizationDescription() {
            return "Player";
        }

        @Override
        public String currentDescription() {
            GameProfile profile = ((MannequinEntityCustomizationInvoker) mannequin).invokeGetMannequinProfile().partialProfile();
            String name = profile.name();
            if (name == null || name.isBlank()) {
                name = mannequin.level().getServer().services().profileResolver()
                        .fetchById(profile.id()).map(GameProfile::name).orElse(null);
            }
            return Objects.requireNonNullElseGet(name, () -> "Unknown player with id " + profile.id());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return Items.PLAYER_HEAD;
        }

        @Override
        public AppearanceCustomization<Mannequin> setNext(Mannequin shopkeeper) {
            String start = currentDescription();
            if (start.length() > 16) {
                start = "username";
            }

            new TextInputGui(player, start, (str) -> {
                if (str.length() > 16) {
                    player.sendSystemMessage(Component.nullToEmpty("Usernames are max 16 characters"));
                    return;
                }
                Optional<GameProfile> profile =
                        shopkeeper.level().getServer().services().profileResolver()
                                .fetchByName(str);

                if(profile.isPresent()) {
                    ResolvableProfile component = ResolvableProfile.createUnresolved(profile.get().id());
                    ((MannequinEntityCustomizationInvoker) shopkeeper).invokeSetMannequinProfile(component);
                } else {
                    player.sendSystemMessage(Component.nullToEmpty("Could not find player with username " + str));
                }
            }, currentGui).open();
            return this;
        }
    }

}
