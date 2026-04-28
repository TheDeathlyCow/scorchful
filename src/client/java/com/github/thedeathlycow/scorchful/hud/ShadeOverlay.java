package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class ShadeOverlay {

    public static final ResourceLocation SHADE_OVERLAY = Scorchful.id("textures/misc/shade_overlay.png");

    public static void renderShadeOverlay(
            GuiGraphics context,
            @Nullable LocalPlayer player,
            BiConsumer<GuiGraphics, Float> renderCallback
    ) {
        if (player != null && !player.isScoping() && SunHatItem.isWearingSunHat(player)) {
            ClientConfig config = Scorchful.getConfig().clientConfig;
            if (config.isSunHatShading()) {
                renderCallback.accept(context, config.getSunHatShadeOpacity());
            }
        }
    }

    private ShadeOverlay() {

    }

}
