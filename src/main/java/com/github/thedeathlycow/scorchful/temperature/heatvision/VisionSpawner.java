package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SMobEffects;
import com.github.thedeathlycow.scorchful.util.SMth;
import org.joml.Vector2i;

import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;

public class VisionSpawner {

    private static final VisionGenerator generator = new VisionGenerator();

    public static void tick(Player player) {

        if (!ScorchfulConfig.getEntityConfig().enableDesertVisions()) {
            return;
        }

        Level world = player.level();

        if (world.isClientSide() || !player.hasEffect(SMobEffects.HEAT_STROKE)) {
            return;
        }

        int sunLight = world.getBrightness(LightLayer.SKY, player.blockPosition()) - world.getSkyDarken();
        if (sunLight < 14) {
            return;
        }

        if (player.tickCount % 20 == 0 && player.getRandom().nextInt(10) == 0) {
            spawnDesertVision((ServerLevel) world, player);
        }
    }

    private static void spawnDesertVision(ServerLevel serverWorld, Player cause) {
        BlockPos pos = chooseVisionPos(serverWorld, cause.blockPosition(), cause.getRandom());
        if (pos == null) {
            return;
        }
        var controller = generator.chooseVision(serverWorld, pos);
        if (controller != null) {
            controller.spawn(cause, serverWorld, pos);
            Scorchful.LOGGER.debug("Spawned a desert vision at " + pos);
        }
    }

    private static BlockPos chooseVisionPos(ServerLevel serverWorld, BlockPos origin, RandomSource random) {
        Vector2i xz = generateXZ(random, origin.getX(), origin.getZ(), 32, 4);
        OptionalInt y = generateY(serverWorld, origin, xz.x, xz.y);
        if (y.isEmpty()) {
            return null;
        }
        return new BlockPos(xz.x, y.getAsInt(), xz.y);
    }

    /**
     * Generates points in a 2D doughnut distribution around the origin
     * <p>
     * Based on approach 1 from <a href="https://codegolf.stackexchange.com/questions/243774/random-point-from-a-2d-donut-distribution">this code golf challenge</a>
     *
     * @param random  random source
     * @param xOrigin x origin
     * @param yOrigin y origin
     * @param radius  radius of the doughnut
     * @param spread  how far from the radius points should spread (normally distributed)
     * @return returns a new vector with point randomly sampled as described
     */
    private static Vector2i generateXZ(RandomSource random, int xOrigin, int yOrigin, double radius, double spread) {
        double a = Mth.nextDouble(random, 0, Math.PI * 2);
        double b = SMth.nextGaussian(random, radius, spread);
        return new Vector2i(
                xOrigin + Mth.floor(b * Math.cos(a)),
                yOrigin + Mth.floor(b * Math.sin(a))
        );
    }

    private static OptionalInt generateY(ServerLevel serverWorld, BlockPos playerOrigin, int visionX, int visionZ) {
        return OptionalInt.of(serverWorld.getHeight(Heightmap.Types.MOTION_BLOCKING, visionX, visionZ));
    }

    private VisionSpawner() {
    }
}
