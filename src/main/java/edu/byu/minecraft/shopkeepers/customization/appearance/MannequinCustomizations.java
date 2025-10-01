package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class MannequinCustomizations {
    public static List<AppearanceCustomization<MannequinEntity>> getMannequinCustomizations(MannequinEntity entity) {
        List<AppearanceCustomization<MannequinEntity>> customizations = new ArrayList<>();
        customizations.add(new MannequinPoseCustomization(entity));
        return customizations;
    }

    private record MannequinPoseCustomization(MannequinPose mannequinPose)
            implements AppearanceCustomization<MannequinEntity> {
        private enum MannequinPose {
            STANDING(EntityPose.STANDING),
            CROUCHING(EntityPose.CROUCHING),
            SWIMMING(EntityPose.SWIMMING),
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

}
