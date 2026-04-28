package com.github.thedeathlycow.scorchful.client.registry;

import com.github.thedeathlycow.scorchful.entity.feature.SunHatFeatureRenderer;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.GiantMobRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Environment(EnvType.CLIENT)
public class SFeatureRenderers {

    public static void registerAll() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof HumanoidMobRenderer<?, ?> bipedEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        bipedEntityRenderer,
                                        new SunHatModel<>(context.bakeLayer(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof PlayerRenderer playerEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        playerEntityRenderer,
                                        new SunHatModel<>(context.bakeLayer(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof ArmorStandRenderer armorStandEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        armorStandEntityRenderer,
                                        new SunHatModel<>(context.bakeLayer(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    } else if (entityRenderer instanceof GiantMobRenderer giantEntityRenderer) {
                        registrationHelper.register(
                                new SunHatFeatureRenderer<>(
                                        giantEntityRenderer,
                                        new SunHatModel<>(context.bakeLayer(SEntityModelLayers.SUN_HAT))
                                )
                        );
                    }
                }
        );
    }

    private SFeatureRenderers() {

    }

}
