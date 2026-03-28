package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.entity.renderlayer.SunHatRenderLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.GiantMobRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

@Environment(EnvType.CLIENT)
public class SFeatureRenderers {

    public static void registerAll() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof HumanoidMobRenderer<?, ?, ?> bipedEntityRenderer) {
                        registrationHelper.register(
                                new SunHatRenderLayer<>(
                                        bipedEntityRenderer,
                                        context.getModelSet()
                                )
                        );
                    } else if (entityRenderer instanceof AvatarRenderer<?> playerEntityRenderer) {
                        registrationHelper.register(
                                new SunHatRenderLayer<>(
                                        playerEntityRenderer,
                                        context.getModelSet()
                                )
                        );
                    } else if (entityRenderer instanceof ArmorStandRenderer armorStandEntityRenderer) {
                        registrationHelper.register(
                                new SunHatRenderLayer<>(
                                        armorStandEntityRenderer,
                                        context.getModelSet()
                                )
                        );
                    } else if (entityRenderer instanceof GiantMobRenderer giantEntityRenderer) {
                        registrationHelper.register(
                                new SunHatRenderLayer<>(
                                        giantEntityRenderer,
                                        context.getModelSet()
                                )
                        );
                    }
                }
        );
    }

    private SFeatureRenderers() {

    }

}
