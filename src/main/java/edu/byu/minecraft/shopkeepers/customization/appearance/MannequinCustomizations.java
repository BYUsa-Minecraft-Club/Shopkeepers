package edu.byu.minecraft.shopkeepers.customization.appearance;

import com.mojang.authlib.GameProfile;
import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.gui.TextInputGui;
import edu.byu.minecraft.shopkeepers.mixin.invoker.MannequinEntityCustomizationInvoker;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MannequinCustomizations {
    public static List<AppearanceCustomization<MannequinEntity>> getMannequinCustomizations(MannequinEntity entity,
                                                                                            ServerPlayerEntity player,
                                                                                            SimpleGui currentGui) {
        List<AppearanceCustomization<MannequinEntity>> customizations = new ArrayList<>();
        if (player.getEntityWorld().getServer().getPlayerManager().isOperator(player.getPlayerConfigEntry())) {
            customizations.add(new Test(entity, player, currentGui));
        }
        customizations.add(new MannequinPoseCustomization(entity));
        return customizations;
    }

    private record MannequinPoseCustomization(MannequinPose mannequinPose)
            implements AppearanceCustomization<MannequinEntity> {
        private enum MannequinPose {
            STANDING(EntityPose.STANDING), CROUCHING(EntityPose.CROUCHING), SWIMMING(EntityPose.SWIMMING),
            GLIDING(EntityPose.GLIDING);

            private final EntityPose pose;

            private MannequinPose(EntityPose pose) {
                this.pose = pose;
            }

            private static MannequinPose of(EntityPose pose) {
                for (MannequinPose mannequinPose : MannequinPose.values()) {
                    if (mannequinPose.pose.equals(pose)) {
                        return mannequinPose;
                    }
                }
                return STANDING;
            }

        }

        private MannequinPoseCustomization(MannequinEntity mannequin) {
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
        public AppearanceCustomization<MannequinEntity> setNext(MannequinEntity shopkeeper) {
            MannequinPose next = CustomizationUtils.nextAlphabetically(mannequinPose, MannequinPose.values());
            shopkeeper.setPose(next.pose);
            return new MannequinPoseCustomization(next);
        }
    }

    private record Test(MannequinEntity mannequin, ServerPlayerEntity player, SimpleGui currentGui)
            implements AppearanceCustomization<MannequinEntity> {

        @Override
        public String customizationDescription() {
            return "Player";
        }

        @Override
        public String currentDescription() {
            GameProfile profile = ((MannequinEntityCustomizationInvoker) mannequin).invokeGetMannequinProfile().getGameProfile();
            String name = profile.name();
            if (name == null || name.isBlank()) {
                name = mannequin.getEntityWorld().getServer().getApiServices().profileResolver()
                        .getProfileById(profile.id()).map(GameProfile::name).orElse(null);
            }
            return Objects.requireNonNullElseGet(name, () -> "Unknown player with id " + profile.id());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return Items.PLAYER_HEAD;
        }

        @Override
        public AppearanceCustomization<MannequinEntity> setNext(MannequinEntity shopkeeper) {
            String start = currentDescription();
            if (start.length() > 16) {
                start = "username";
            }

            new TextInputGui(player, start, (str) -> {
                if (str.length() > 16) {
                    player.sendMessage(Text.of("Usernames are max 16 characters"));
                    return;
                }
                Optional<GameProfile> profile =
                        shopkeeper.getEntityWorld().getServer().getApiServices().profileResolver()
                                .getProfileByName(str);

                if(profile.isPresent()) {
                    ProfileComponent component = ProfileComponent.ofDynamic(profile.get().id());
                    ((MannequinEntityCustomizationInvoker) shopkeeper).invokeSetMannequinProfile(component);
                } else {
                    player.sendMessage(Text.of("Could not find player with username " + str));
                }
            }, currentGui).open();
            return this;
        }
    }

}
