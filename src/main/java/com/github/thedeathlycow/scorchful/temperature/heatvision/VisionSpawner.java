package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.util.SMth;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.joml.Vector2i;

import java.util.OptionalInt;

public class VisionSpawner {

    private static final VisionGenerator generator = new VisionGenerator();

    public static void tick(PlayerEntity player) {
        World world = player.getWorld();

        if (world.isClient() || !player.hasStatusEffect(SStatusEffects.HEAT_STROKE)) {
            return;
        }

        if (player.age % 20 == 0 && player.getRandom().nextInt(10) == 0) {
            spawnDesertVision((ServerWorld) world, player);
        }
    }

    private static void spawnDesertVision(ServerWorld serverWorld, PlayerEntity cause) {
        BlockPos pos = chooseVisionPos(serverWorld, cause.getBlockPos(), cause.getRandom());
        if (pos == null) {
            return;
        }
        var controller = generator.chooseVision(serverWorld, pos);
        if (controller != null) {
            controller.spawn(cause, serverWorld, pos);
            Scorchful.LOGGER.debug("Spawned a desert vision at " + pos);
        }
    }

    private static BlockPos chooseVisionPos(ServerWorld serverWorld, BlockPos origin, Random random) {
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
    private static Vector2i generateXZ(Random random, int xOrigin, int yOrigin, double radius, double spread) {
        double a = MathHelper.nextDouble(random, 0, Math.PI * 2);
        double b = SMth.nextGaussian(random, radius, spread);
        return new Vector2i(
                xOrigin + MathHelper.floor(b * Math.cos(a)),
                yOrigin + MathHelper.floor(b * Math.sin(a))
        );
    }

    private static OptionalInt generateY(ServerWorld serverWorld, BlockPos playerOrigin, int visionX, int visionZ) {

        if (serverWorld.getDimension().hasCeiling()) {
            final int range = 5;
            var mutable = new BlockPos.Mutable(visionX, playerOrigin.getY() - range, visionZ);
            for (int dy = -range; dy <= range; dy++) {
                var state = serverWorld.getBlockState(mutable);
                if (state.isAir() && !serverWorld.isOutOfHeightLimit(mutable)) {
                    return OptionalInt.of(mutable.getY());
                }
                mutable.set(visionX, playerOrigin.getY() + dy, visionZ);
            }

            return OptionalInt.empty();
        }

        return OptionalInt.of(serverWorld.getTopY(Heightmap.Type.WORLD_SURFACE, visionX, visionZ));
    }

    private VisionSpawner() {
    }


}
