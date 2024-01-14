package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ShadeOverlay {

    public static final Identifier SHADE_OVERLAY = Scorchful.id("textures/misc/shade_overlay.png");

    public static void renderShadeOverlay(
            DrawContext context,
            @Nullable ClientPlayerEntity player,
            BiConsumer<DrawContext, Float> renderCallback
    ) {


        if (player != null && SunHatItem.isWearingSunHat(player)) {
            ClientConfig config = Scorchful.getConfig().clientConfig;
            if (config.isSunHatShading()) {
                renderCallback.accept(context, config.getSunHatShadeOpacity());
            }
        }
    }

    private ShadeOverlay() {

    }

}
