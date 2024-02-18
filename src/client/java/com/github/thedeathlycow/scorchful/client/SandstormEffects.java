package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class SandstormEffects {

    public static final Vec3d REGULAR_SANDSTORM_SKY_COLOR = Vec3d.unpackRgb(0xDBD3A0);
    public static final Vector3f REGULAR_SANDSTORM_FOG_COLOR = Vec3d.unpackRgb(0xD9AA84).toVector3f();

    public static void onClientWorldTick(ClientWorld clientWorld) {
        if (!clientWorld.isRaining()) {
            return;
        }

        final ClientConfig config = Scorchful.getConfig().clientConfig;
        final int renderDistance = config.getSandStormParticleRenderDistance();
        if (!config.isSandstormParticlesEnabled() || renderDistance <= 0) {
            return; // config disabled
        }

        final MinecraftClient gameClient = MinecraftClient.getInstance();
        final ClientPlayerEntity mainPlayer = gameClient.player;
        final Camera camera = gameClient.gameRenderer.getCamera();
        if (mainPlayer == null || camera == null) {
            return; // no player or camera for whatever reason
        }


        // main particle loop

        final BlockPos cameraPos = camera.getBlockPos();
        final BlockPos.Mutable pos = new BlockPos.Mutable();
        final ParticleEffect particle = new DustParticleEffect(REGULAR_SANDSTORM_FOG_COLOR, config.getSandStormParticleScale());
        final int rarity = config.getSandStormParticleRarity();
        final int cameraY = cameraPos.getY();
        final float particleVelocity = config.getSandStormParticleVelocity();

        for (int x = cameraPos.getX() - renderDistance; x < cameraPos.getX() + renderDistance; x++) {
            for (int z = cameraPos.getZ() - renderDistance; z < cameraPos.getZ() + renderDistance; z++) {
                int y = cameraY + clientWorld.random.nextBetween(-renderDistance / 2, (renderDistance + 1) / 2);
                y = Math.max(y, clientWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z));
                pos.set(x, y, z);
                addParticle(clientWorld, particle, pos, rarity, particleVelocity);
            }
        }
    }


    private static void addParticle(ClientWorld world, ParticleEffect particle, BlockPos pos, int rarity, float velocity) {
        if (Sandstorms.isSandStorming(world, pos) && world.random.nextInt(rarity) == 0) {
            world.addParticle(
                    particle,
                    pos.getX() + world.random.nextDouble(),
                    pos.getY() + world.random.nextDouble(),
                    pos.getZ() + world.random.nextDouble(),
                    velocity, 0, 0
            );
        }
    }

    private SandstormEffects() {

    }
}
