package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

public class AmbientTemperatureController extends EnvironmentControllerDecorator {


    private static final int BLOCK_LIGHT_DIVISOR = 4;
    private static final int HEAT_HEIGHT_SLOPE = -5;
    private static final int MAX_HEAT_FROM_HEIGHT = 3;


    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    public AmbientTemperatureController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public int getFloorTemperature(LivingEntity entity, World world, BlockState state, BlockPos pos) {
        if (state.isIn(SBlockTags.HEAVY_ICE)) {
            ScorchfulConfig config = Scorchful.getConfig();
            return -config.heatingConfig.getCoolingFromIce();
        } else {
            return controller.getFloorTemperature(entity, world, state, pos);
        }
    }

    @Override
    public int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature) {
        if (player.thermoo$isCold() && localTemperature < 0) {
            return controller.getEnvironmentTemperatureForPlayer(player, localTemperature);
        }

        return localTemperature;
    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        DimensionType dimensionType = world.getDimension();

        if (dimensionType.natural()) {
            return getNaturalWorldTemperatureChange(world, pos);
        } else if (world.getDimension().ultrawarm()) {
            return getNetherTemperatureChange(world, pos);
        } else {
            return controller.getLocalTemperatureChange(world, pos);
        }
    }

    private int getNetherTemperatureChange(World world, BlockPos pos) {
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);

        // assume no sea level
        int distanceToLavaLevel = Integer.MAX_VALUE;

        // generally this should be true, since we always execute on the server
        if (world.getChunkManager() instanceof ServerChunkManager serverChunkManager) {
            int height = pos.getY();
            int seaLevel = serverChunkManager.getChunkGenerator().getSeaLevel();

            distanceToLavaLevel = Math.max(height - seaLevel, 0);
        }

        return Math.max(
                blockLight / BLOCK_LIGHT_DIVISOR,
                distanceToLavaLevel != 0
                        ? ((distanceToLavaLevel / HEAT_HEIGHT_SLOPE) + MAX_HEAT_FROM_HEIGHT)
                        : MAX_HEAT_FROM_HEIGHT
        );
    }

    private int getNaturalWorldTemperatureChange(World world, BlockPos pos) {
        // initialize to base
        int warmth = controller.getLocalTemperatureChange(world, pos);

        RegistryEntry<Biome> biome = world.getBiome(pos);
        ScorchfulConfig config = Scorchful.getConfig();

        if (biome.isIn(SBiomeTags.WARM_BIOMES) || biome.value().getTemperature() >= 0.95f) {
            int skylight = world.getLightLevel(LightType.SKY, pos) - world.getAmbientDarkness();

            int minLevel = config.heatingConfig.getGetMinSkyLightLevelForHeat();

            if (skylight >= minLevel) {
                warmth += config.heatingConfig.getHeatFromSun();

                if (biome.isIn(SBiomeTags.SCORCHING_BIOMES)) {
                    warmth += config.heatingConfig.getScorchingBiomeHeatIncrease();
                }
            }
        }

        return warmth;
    }

}
