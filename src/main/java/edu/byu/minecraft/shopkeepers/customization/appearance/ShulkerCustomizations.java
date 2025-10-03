package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils;
import edu.byu.minecraft.shopkeepers.customization.CustomizationUtils.DyeColorWithNone;
import edu.byu.minecraft.shopkeepers.mixin.invoker.ShulkerEntityVariationSetter;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShulkerCustomizations {

    public static List<AppearanceCustomization<ShulkerEntity>> getShulkerCustomizations(ShulkerEntity shulker) {
        List<AppearanceCustomization<ShulkerEntity>> customizations = new ArrayList<>();
        customizations.add(ShulkerColorCustomization.forShulker(shulker));
        customizations.add(new ShulkerAttachedFaceCustomization(shulker));
        customizations.add(new ShulkerPeekHeightCustomization(shulker));
        return customizations;
    }

    private record ShulkerColorCustomization(DyeColorWithNone color) implements AppearanceCustomization<ShulkerEntity> {

        private static ShulkerColorCustomization forShulker(ShulkerEntity shulker) {
            DyeColor color = shulker.getColor();
            return new ShulkerColorCustomization(color == null ? DyeColorWithNone.NONE : DyeColorWithNone.valueOf(color.name()));
        }

        @Override
        public String customizationDescription() {
            return "Color";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(color.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            if (color == DyeColorWithNone.NONE) return Items.BARRIER;
            return CustomizationUtils.getDyeItem(DyeColor.valueOf(color.name()));
        }

        @Override
        public AppearanceCustomization<ShulkerEntity> setNext(ShulkerEntity shopkeeper) {
            DyeColorWithNone next = CustomizationUtils.nextEnum(color, DyeColorWithNone.values());
            Optional<DyeColor> colorOptional =
                    (next == DyeColorWithNone.NONE) ? Optional.empty() : Optional.of(DyeColor.valueOf(next.name()));
            ((ShulkerEntityVariationSetter) shopkeeper).invokeSetColor(colorOptional);
            return new ShulkerColorCustomization(next);
        }
    }

    private record ShulkerAttachedFaceCustomization(Direction direction) implements
            AppearanceCustomization<ShulkerEntity> {

        private ShulkerAttachedFaceCustomization(ShulkerEntity shulker) {
            this(shulker.getAttachedFace());
        }

        @Override
        public String customizationDescription() {
            return "Attached Face";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(direction.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (direction) {
                case DOWN -> Items.LAVA_BUCKET;
                case UP -> Items.GOLD_BLOCK;
                case NORTH -> Items.SNOW_BLOCK;
                case SOUTH -> Items.TERRACOTTA;
                case WEST -> Items.COMPARATOR;
                case EAST -> Items.SALMON;
            };
        }

        @Override
        public AppearanceCustomization<ShulkerEntity> setNext(ShulkerEntity shopkeeper) {
            Direction next = CustomizationUtils.nextEnum(direction(), Direction.values());
            ((ShulkerEntityVariationSetter) shopkeeper).invokeSetAttachedFace(next);
            return new ShulkerAttachedFaceCustomization(next);
        }
    }

    private record ShulkerPeekHeightCustomization(PeekHeight peekHeight) implements
            AppearanceCustomization<ShulkerEntity> {

        private enum PeekHeight {
            CLOSED, PEEKING, OPEN
        }

        private ShulkerPeekHeightCustomization(ShulkerEntity shulker) {
            this(switch (((ShulkerEntityVariationSetter) shulker).invokeGetPeekAmount()) {
                case 0 -> PeekHeight.CLOSED;
                case 30 -> PeekHeight.PEEKING;
                case 100 -> PeekHeight.OPEN;
                default -> throw new IllegalStateException("Unexpected value: " + shulker);
            });
        }

        @Override
        public String customizationDescription() {
            return "Peek Height";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(peekHeight.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return switch (peekHeight) {
                case CLOSED -> Items.SHIELD;
                case PEEKING -> Items.SHULKER_SHELL;
                case OPEN -> Items.SHULKER_BOX;
            };
        }

        @Override
        public AppearanceCustomization<ShulkerEntity> setNext(ShulkerEntity shopkeeper) {
            PeekHeight next = CustomizationUtils.nextEnum(peekHeight, PeekHeight.values());
            ((ShulkerEntityVariationSetter) shopkeeper).invokeSetPeekAmount(switch (next) {
                case CLOSED -> 0;
                case PEEKING -> 30;
                case OPEN -> 100;
            });
            return new ShulkerPeekHeightCustomization(next);
        }
    }

}
