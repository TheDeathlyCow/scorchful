package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.entity.feature.SunHatFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

@Environment(EnvType.CLIENT)
public class SFeatureRenderers {

    public static void registerAll() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof BipedEntityRenderer<?, ?, ?> bipedEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        bipedEntityRenderer,
                                        context.getEntityModels()
                                )
                        );
                    } else if (entityRenderer instanceof PlayerEntityRenderer<?> playerEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        playerEntityRenderer,
                                        context.getEntityModels()
                                )
                        );
                    } else if (entityRenderer instanceof ArmorStandEntityRenderer armorStandEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        armorStandEntityRenderer,
                                        context.getEntityModels()
                                )
                        );
                    } else if (entityRenderer instanceof GiantEntityRenderer giantEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        giantEntityRenderer,
                                        context.getEntityModels()
                                )
                        );
                    }
                }
        );
    }

    private SFeatureRenderers() {

    }

}
