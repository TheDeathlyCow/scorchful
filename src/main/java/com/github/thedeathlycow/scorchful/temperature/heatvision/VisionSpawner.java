package com.github.thedeathlycow.scorchful.temperature.heatvision;

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
        var controller = generator.chooseVision(serverWorld, pos);
        if (controller != null) {
            controller.spawn(cause, serverWorld, pos);
        }
    }

    private static BlockPos chooseVisionPos(ServerWorld serverWorld, BlockPos origin, Random random) {
        var xz = generateXZ(random, origin.getX(), origin.getZ(), 32, 4);
        int y = serverWorld.getTopY(Heightmap.Type.WORLD_SURFACE, xz.x, xz.y);
        return new BlockPos(xz.x, y, xz.y);
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

    private VisionSpawner() {
    }


}
