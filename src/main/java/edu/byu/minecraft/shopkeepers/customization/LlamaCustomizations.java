package edu.byu.minecraft.shopkeepers.customization;

import edu.byu.minecraft.shopkeepers.mixin.invoker.LlamaEntityVariantSetter;
import net.minecraft.block.MapColor;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class LlamaCustomizations {

    public static List<ShopkeeperCustomization<LlamaEntity>> getLlamaCustomizations(LlamaEntity llama) {
        List<ShopkeeperCustomization<LlamaEntity>> customizations = new ArrayList<>();
        customizations.add(new LlamaVariantCustomization(llama.getVariant()));
        customizations.add(LlamaCarpetCustomization.forLlama(llama));
        customizations.add(new AbstractDonkeyCustomizations.ChestedCustomization<>(llama));
        return customizations;
    }

    private record LlamaVariantCustomization(LlamaEntity.Variant variant) implements ShopkeeperCustomization<LlamaEntity> {

        @Override
            public String customizationDescription() {
                return "Variant";
            }

            @Override
            public String currentDescription() {
                return CustomizationUtils.capitalize(variant.name());
            }

            @Override
            public Item getCurrentRepresentationItem() {
                return switch (variant) {
                    case CREAMY -> Items.YELLOW_DYE;
                    case WHITE -> Items.WHITE_DYE;
                    case BROWN -> Items.BROWN_DYE;
                    case GRAY -> Items.LIGHT_GRAY_DYE;
                };
            }

            @Override
            public ShopkeeperCustomization<LlamaEntity> setNext(LlamaEntity shopkeeper) {
                LlamaEntity.Variant next = CustomizationUtils.nextAlphabetically(shopkeeper.getVariant());
                ((LlamaEntityVariantSetter) shopkeeper).invokeSetVariant(next);
                return new LlamaVariantCustomization(next);
            }
        }

    private record LlamaCarpetCustomization(CarpetOption carpet) implements ShopkeeperCustomization<LlamaEntity> {
        private enum CarpetOption {
            NONE, WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN,
            GREEN, RED, BLACK
        }

        private static LlamaCarpetCustomization forLlama(LlamaEntity llama) {
            ItemStack carpetStack = llama.getBodyArmor();
            if(carpetStack == null || carpetStack.isEmpty()) return new LlamaCarpetCustomization(CarpetOption.NONE);
            Item carpetItem = carpetStack.getItem();
            if(carpetItem == Items.WHITE_CARPET) return new LlamaCarpetCustomization(CarpetOption.WHITE);
            else if(carpetItem == Items.ORANGE_CARPET) return new LlamaCarpetCustomization(CarpetOption.ORANGE);
            else if (carpetItem == Items.MAGENTA_CARPET) return new LlamaCarpetCustomization(CarpetOption.MAGENTA);
            else if (carpetItem == Items.LIGHT_BLUE_CARPET) return new LlamaCarpetCustomization(CarpetOption.LIGHT_BLUE);
            else if (carpetItem == Items.YELLOW_CARPET) return new LlamaCarpetCustomization(CarpetOption.YELLOW);
            else if (carpetItem == Items.LIME_CARPET) return new LlamaCarpetCustomization(CarpetOption.LIME);
            else if (carpetItem == Items.PINK_CARPET) return new LlamaCarpetCustomization(CarpetOption.PINK);
            else if (carpetItem == Items.GRAY_CARPET) return new LlamaCarpetCustomization(CarpetOption.GRAY);
            else if (carpetItem == Items.LIGHT_GRAY_CARPET) return new LlamaCarpetCustomization(CarpetOption.LIGHT_GRAY);
            else if (carpetItem == Items.CYAN_CARPET) return new LlamaCarpetCustomization(CarpetOption.CYAN);
            else if (carpetItem == Items.PURPLE_CARPET) return new LlamaCarpetCustomization(CarpetOption.PURPLE);
            else if (carpetItem == Items.BLUE_CARPET) return new LlamaCarpetCustomization(CarpetOption.BLUE);
            else if (carpetItem == Items.BROWN_CARPET) return new LlamaCarpetCustomization(CarpetOption.BROWN);
            else if (carpetItem == Items.GREEN_CARPET) return new LlamaCarpetCustomization(CarpetOption.GREEN);
            else if (carpetItem == Items.RED_CARPET) return new LlamaCarpetCustomization(CarpetOption.RED);
            else if (carpetItem == Items.BLACK_CARPET) return new LlamaCarpetCustomization(CarpetOption.BLACK);
            else throw new IllegalStateException("unknown carpet item: " + carpetItem);
        }

        @Override
        public String customizationDescription() {
            return "Carpet";
        }

        @Override
        public String currentDescription() {
            return CustomizationUtils.capitalize(carpet.name());
        }

        @Override
        public Item getCurrentRepresentationItem() {
            if (carpet == CarpetOption.NONE) return Items.BARRIER;
            return CustomizationUtils.getDyeItem(DyeColor.valueOf(carpet.name()));
        }

        @Override
        public ShopkeeperCustomization<LlamaEntity> setNext(LlamaEntity shopkeeper) {
            CarpetOption next = CustomizationUtils.nextAlphabetically(forLlama(shopkeeper).carpet);
            shopkeeper.equipBodyArmor(new ItemStack(switch (next) {
                case NONE -> ItemStack.EMPTY.getItem();
                case WHITE -> Items.WHITE_CARPET;
                case ORANGE -> Items.ORANGE_CARPET;
                case MAGENTA -> Items.MAGENTA_CARPET;
                case LIGHT_BLUE -> Items.LIGHT_BLUE_CARPET;
                case YELLOW -> Items.YELLOW_CARPET;
                case LIME -> Items.LIME_CARPET;
                case PINK -> Items.PINK_CARPET;
                case GRAY -> Items.GRAY_CARPET;
                case LIGHT_GRAY -> Items.LIGHT_GRAY_CARPET;
                case CYAN -> Items.CYAN_CARPET;
                case PURPLE -> Items.PURPLE_CARPET;
                case BLUE -> Items.BLUE_CARPET;
                case BROWN -> Items.BROWN_CARPET;
                case GREEN -> Items.GREEN_CARPET;
                case RED -> Items.RED_CARPET;
                case BLACK -> Items.BLACK_CARPET;
            }));
            return new LlamaCarpetCustomization(next);
        }
    }

}
