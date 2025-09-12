package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.mixin.invoker.GlowSquidEntityDarkTicksRemainingSetter;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class GlowSquidCustomizations {

    public static List<AppearanceCustomization<GlowSquidEntity>> getGlowSquidCustomizations(GlowSquidEntity glowSquid) {
        List<AppearanceCustomization<GlowSquidEntity>> customizations = new ArrayList<>();
        customizations.add(new GlowSquidGlowingCustomization(glowSquid));
        return customizations;
    }

    private record GlowSquidGlowingCustomization(boolean isGlowing)
            implements AppearanceCustomization<GlowSquidEntity> {

        private GlowSquidGlowingCustomization(GlowSquidEntity glowSquid) {
            this(glowSquid.getDarkTicksRemaining() <= 0);
        }
        @Override
        public String customizationDescription() {
            return "Glowing";
        }

        @Override
        public String currentDescription() {
            return isGlowing ? "Glowing" : "Dark";
        }

        @Override
        public Item getCurrentRepresentationItem() {
            return isGlowing ? Items.SOUL_TORCH : Items.STICK;
        }

        @Override
        public AppearanceCustomization<GlowSquidEntity> setNext(GlowSquidEntity shopkeeper) {
            ((GlowSquidEntityDarkTicksRemainingSetter) shopkeeper).invokeSetDarkTicksRemaining(isGlowing ? 2099999999 : 0);
            return new GlowSquidGlowingCustomization(!isGlowing);
        }
    }
}
