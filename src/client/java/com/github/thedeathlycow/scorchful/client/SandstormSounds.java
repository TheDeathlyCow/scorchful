package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.Optional;

public class SandstormSounds {

    public static final SandstormSounds INSTANCE = new SandstormSounds();

    private static final int MAX_SOUND_Y_DIFF = 10;

    private static final int MAX_XZ_OFFSET = 10;

    private int timer = 0;

    public void tick(ClientWorld world) {

        if (world.getTickManager().isFrozen() || !Scorchful.getConfig().clientConfig.isSandstormSoundsEnabled()) {
            return;
        }

        final MinecraftClient gameClient = MinecraftClient.getInstance();
        final Camera camera = gameClient.gameRenderer.getCamera();
        if (camera == null) {
            return; // no camera for whatever reason
        }

        if (!world.isRaining()) {
            return;
        }

        if (world.random.nextInt(3) >= this.timer) {
            this.timer++;
            return;
        }
        this.timer = 0;

        this.chooseSpotForWindSound(world, camera).ifPresent(
                pos -> {
                    world.playSoundAtBlockCenter(
                            pos,
                            SSoundEvents.WEATHER_SANDSTORM,
                            SoundCategory.WEATHER,
                            0.1f, 0.5f,
                            false
                    );
                }
        );

    }

    private Optional<BlockPos> chooseSpotForWindSound(ClientWorld world, Camera camera) {

        BlockPos cameraPos = camera.getBlockPos();

        int dx = world.random.nextBetween(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        int dy = world.random.nextBetween(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        int dz = world.random.nextBetween(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        BlockPos soundPos = cameraPos.add(dx, dy, dz);

        if (Sandstorms.getCurrentSandStorm(world, soundPos) != Sandstorms.SandstormType.NONE) {
            return Optional.of(soundPos);
        }

        return Optional.empty();
    }

    private SandstormSounds() {

    }
}
