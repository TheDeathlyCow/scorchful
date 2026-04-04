package com.github.thedeathlycow.scorchful.client.registry;

import com.github.thedeathlycow.scorchful.client.entity.renderlayer.SunHatRenderLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

@Environment(EnvType.CLIENT)
public class SFeatureRenderers {
    public static void registerAll() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            switch (entityRenderer) {
                case AvatarRenderer<?> avatarRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    avatarRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.GENERIC_SUN_HAT,
                                    SEntityModelLayers.GENERIC_BABY_SUN_HAT
                            )
                    );
                }
                case ArmorStandRenderer armorStandRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    armorStandRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.GENERIC_SUN_HAT,
                                    SEntityModelLayers.GENERIC_BABY_SUN_HAT
                            )
                    );
                }
                case GiantMobRenderer giantEntityRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    giantEntityRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.GENERIC_SUN_HAT,
                                    SEntityModelLayers.GENERIC_BABY_SUN_HAT
                            )
                    );
                }
                case HuskRenderer huskRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    huskRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.HUSK_SUN_HAT,
                                    SEntityModelLayers.HUSK_BABY_SUN_HAT
                            )
                    );
                }
                case PiglinRenderer piglinRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    piglinRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.PIGLIN_SUN_HAT,
                                    SEntityModelLayers.PIGLIN_BABY_SUN_HAT
                            )
                    );
                }
                case ZombifiedPiglinRenderer piglinRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    piglinRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.PIGLIN_SUN_HAT,
                                    SEntityModelLayers.PIGLIN_BABY_SUN_HAT
                            )
                    );
                }
                case ZombieVillagerRenderer zombieVillagerRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    zombieVillagerRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.ZOMBIE_VILLAGER_SUN_HAT,
                                    SEntityModelLayers.ZOMBIE_VILLAGER_BABY_SUN_HAT
                            )
                    );
                }
                case WitherSkeletonRenderer witherSkeletonRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    witherSkeletonRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.WITHER_SKELETON_SUN_HAT,
                                    SEntityModelLayers.WITHER_SKELETON_SUN_HAT
                            )
                    );
                }
                case HumanoidMobRenderer<?, ?, ?> humanoidMobRenderer -> {
                    registrationHelper.register(
                            new SunHatRenderLayer<>(
                                    humanoidMobRenderer,
                                    context.getModelSet(),
                                    SEntityModelLayers.GENERIC_SUN_HAT,
                                    SEntityModelLayers.GENERIC_BABY_SUN_HAT
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
