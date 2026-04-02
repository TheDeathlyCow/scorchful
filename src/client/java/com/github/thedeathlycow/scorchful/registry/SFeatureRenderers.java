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
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            switch (entityRenderer) {
                case HumanoidMobRenderer<?, ?, ?> humanoidMobRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    humanoidMobRenderer,
                                    context.getModelSet()
                            )
                    );
                }
                case AvatarRenderer<?> avatarRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    avatarRenderer,
                                    context.getModelSet()
                            )
                    );
                }
                case ArmorStandRenderer armorStandRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    armorStandRenderer,
                                    context.getModelSet()
                            )
                    );
                }
                case GiantMobRenderer giantEntityRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    giantEntityRenderer,
                                    context.getModelSet()
                            )
                    );
                }
                default -> {
                }
            }
        });
    }

    private SFeatureRenderers() {

    }

}
