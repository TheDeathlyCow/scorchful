package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.entity.feature.SunHatFeatureRenderer;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.GiantEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

@Environment(EnvType.CLIENT)
public class SFeatureRenderers {

    public static void registerAll() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof BipedEntityRenderer<?, ?> bipedEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        bipedEntityRenderer,
                                        new SunHatModel<>(context.getPart(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof PlayerEntityRenderer playerEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        playerEntityRenderer,
                                        new SunHatModel<>(context.getPart(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof ArmorStandEntityRenderer armorStandEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        armorStandEntityRenderer,
                                        new SunHatModel<>(context.getPart(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof GiantEntityRenderer giantEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        giantEntityRenderer,
                                        new SunHatModel<>(context.getPart(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    }
                }
        );
    }

    private SFeatureRenderers() {

    }

}
