package edu.byu.minecraft.shopkeepers.customization;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import edu.byu.minecraft.shopkeepers.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.passive.ChickenVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ChickenCustomizations {
    public static List<ShopkeeperCustomization<ChickenEntity>> getChickenCustomizations(ChickenEntity entity) {
        List<ShopkeeperCustomization<ChickenEntity>> customizations = new ArrayList<>();
        customizations.add(new ChickenVariantCustomization(entity));
        return customizations;
    }

    private record ChickenVariantCustomization(Variant variant)
            implements ShopkeeperCustomization<ChickenEntity> {
        private enum Variant {
            TEMPERATE(ChickenVariants.TEMPERATE),
            WARM(ChickenVariants.WARM),
            COLD(ChickenVariants.COLD);
            
            private final RegistryKey<ChickenVariant> key;
            private Variant(RegistryKey<ChickenVariant> key) {
                this.key = key;
            }

            private static Variant of(RegistryKey<ChickenVariant> key) {
                for (Variant variant : Variant.values()) {
                    if (variant.key.equals(key)) {
                        return variant;
                    }
                }
                throw new IllegalArgumentException("Unknown variant " + key);
            }
        }

        private ChickenVariantCustomization(ChickenEntity chicken) {
            this(Variant.of(chicken.getVariant().getKey().orElseThrow()));
        }

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
                case TEMPERATE -> Items.WHITE_DYE;
                case WARM -> Items.ORANGE_DYE;
                case COLD -> Items.GRAY_DYE;
            };
        }

        @Override
        public ShopkeeperCustomization<ChickenEntity> setNext(ChickenEntity shopkeeper) {
            Variant current = Variant.of(shopkeeper.getVariant().getKey().orElseThrow());
            Variant next = CustomizationUtils.nextAlphabetically(current);
            shopkeeper.setVariant(shopkeeper.getRegistryManager().getEntryOrThrow(next.key));
            return new ChickenVariantCustomization(next);
        }
    }

}
