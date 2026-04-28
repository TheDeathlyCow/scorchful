package com.github.thedeathlycow.scorchful.client.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

public class SandstormSounds {

    public static final SandstormSounds INSTANCE = new SandstormSounds();

    private static final int MAX_SOUND_Y_DIFF = 10;

    private static final int MAX_XZ_OFFSET = 10;

    private int timer = 0;

    public void tick(ClientLevel world) {

        if (world.tickRateManager().isFrozen() || !Scorchful.getConfig().clientConfig.isSandstormSoundsEnabled()) {
            return;
        }

        final Minecraft gameClient = Minecraft.getInstance();
        final Camera camera = gameClient.gameRenderer.getMainCamera();
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
                    world.playLocalSound(
                            pos,
                            SSoundEvents.WEATHER_SANDSTORM,
                            SoundSource.WEATHER,
                            0.1f, 0.5f,
                            false
                    );
                }
        );

    }

    private Optional<BlockPos> chooseSpotForWindSound(ClientLevel world, Camera camera) {

        BlockPos cameraPos = camera.getBlockPosition();

        int dx = world.random.nextIntBetweenInclusive(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        int dy = world.random.nextIntBetweenInclusive(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        int dz = world.random.nextIntBetweenInclusive(-MAX_XZ_OFFSET, MAX_SOUND_Y_DIFF);
        BlockPos soundPos = cameraPos.offset(dx, dy, dz);

        if (Sandstorms.getCurrentSandStorm(world, soundPos) != Sandstorms.SandstormType.NONE) {
            return Optional.of(soundPos);
        }

        return Optional.empty();
    }

    private SandstormSounds() {

    }
}
