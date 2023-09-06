package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

/**
 * Duplicates the local temperature change computation from Frostiful. Disabled if that mod is enabled for deterministic
 * config ordering.
 */
public class FrostifulTemperatureController extends EnvironmentControllerDecorator {
    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    public FrostifulTemperatureController(com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController controller) {
        super(controller);
    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        DimensionType dimensionType = world.getDimension();
        if (dimensionType.natural()) {
            Biome biome = world.getBiome(pos).value();
            return getTempChangeFromBiomeTemperature(world, biome.getTemperature(), biome.hasPrecipitation());
        } else if (dimensionType.ultrawarm()) {
            return Scorchful.getConfig().environmentConfig.getUltrawarmWarmRate();
        }
        return controller.getLocalTemperatureChange(world, pos);
    }

    /**
     * Computes the base temperature change for a biomeM
     *
     * @param temperature Float value 0.75 - 2
     * @param isDryBiome  If the biome does not have rain
     * @return Returns temp change
     */
    private int getTempChangeFromBiomeTemperature(World world, float temperature, boolean isDryBiome) {
        ScorchfulConfig config = Scorchful.getConfig();
        double mul = config.environmentConfig.getBiomeTemperatureMultiplier();
        double cutoff = config.environmentConfig.getPassiveFreezingCutoffTemp();

        double tempShift = 0.0;
        if (world.isNight() && config.environmentConfig.doDryBiomeNightFreezing()) {
            if (isDryBiome) {
                temperature = Math.min(temperature, config.environmentConfig.getDryBiomeNightTemperature());
            } else {
                tempShift = config.environmentConfig.getNightTimeTemperatureDecrease();
            }
        }

        return MathHelper.floor(mul * (temperature - cutoff - tempShift));
    }
}
