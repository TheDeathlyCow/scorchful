package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.SeasonsConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Cooling {

    public static void tick(LivingEntity entity) {
        if (entity.thermoo$getTemperature() > 0 && entity.thermoo$isWet()) {
            tickWetAndWarm(entity);
        }
    }

    private static void tickWetAndWarm(LivingEntity entity) {
        ScorchfulConfig config = Scorchful.getConfig();

        int temperatureChange = config.thirstConfig.getTemperatureFromWetness();
        World world = entity.getWorld();
        if (!entity.isSubmergedInWater()) {
            float efficiency = getSweatEfficiency(world, entity.getBlockPos(), config);
            temperatureChange = MathHelper.floor(temperatureChange * efficiency);
        }

        entity.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
    }

    private static float getSweatEfficiency(World world, BlockPos pos, ScorchfulConfig config) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        if (biome.isIn(SBiomeTags.HUMID_BIOMES)) {
            return getSeasonalSweatEfficiency(world, pos, config);
        }

        return 1f;
    }

    private static float getSeasonalSweatEfficiency(World world, BlockPos pos, ScorchfulConfig config) {
        float baseEfficiency = config.thirstConfig.getHumidBiomeSweatEfficiency();
        SeasonsConfig seasonsConfig = config.integrationConfig.seasonsConfig;

        if (!seasonsConfig.enableSeasonsIntegration()) {
            return baseEfficiency;
        }

        ThermooSeason season = ThermooSeason.getCurrentTropicalSeason(world, pos).orElse(null);
        return switch (season) {
            case TROPICAL_WET -> seasonsConfig.getWetSeasonHumidBiomeSweatEfficiency();
            case TROPICAL_DRY -> seasonsConfig.getDrySeasonHumidBiomeSweatEfficiency();
            case null, default -> baseEfficiency;
        };
    }

    private Cooling() {

    }

}
