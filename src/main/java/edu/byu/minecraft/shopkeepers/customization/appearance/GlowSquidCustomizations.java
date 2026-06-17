package edu.byu.minecraft.shopkeepers.customization.appearance;

import edu.byu.minecraft.shopkeepers.mixin.invoker.GlowSquidEntityDarkTicksRemainingSetter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GlowSquidCustomizations {

    public static List<AppearanceCustomization<GlowSquid>> getGlowSquidCustomizations(GlowSquid glowSquid) {
        List<AppearanceCustomization<GlowSquid>> customizations = new ArrayList<>();
        customizations.add(new GlowSquidGlowingCustomization(glowSquid));
        return customizations;
    }

    private record GlowSquidGlowingCustomization(boolean isGlowing)
            implements AppearanceCustomization<GlowSquid> {

        private GlowSquidGlowingCustomization(GlowSquid glowSquid) {
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
        public AppearanceCustomization<GlowSquid> setNext(GlowSquid shopkeeper) {
            ((GlowSquidEntityDarkTicksRemainingSetter) shopkeeper).invokeSetDarkTicksRemaining(isGlowing ? 2099999999 : 0);
            return new GlowSquidGlowingCustomization(!isGlowing);
        }
    }
}
