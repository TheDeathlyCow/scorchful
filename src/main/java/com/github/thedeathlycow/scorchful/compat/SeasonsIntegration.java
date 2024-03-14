package com.github.thedeathlycow.scorchful.compat;

import io.github.lucaargolo.seasons.FabricSeasons;
import net.minecraft.world.World;

public class SeasonsIntegration {


    public static AdaptedSeason getSeason(World world) {
        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.FABRIC_SEASONS_ID)) {
            return switch (FabricSeasons.getCurrentSeason(world)) {
                case SUMMER -> AdaptedSeason.SUMMER;
                case WINTER -> AdaptedSeason.WINTER;
                case SPRING -> AdaptedSeason.SPRING;
                case FALL -> AdaptedSeason.AUTUMN;
                default -> AdaptedSeason.NONE;
            };
        }
        return AdaptedSeason.NONE;
    }

    public enum AdaptedSeason {
        NONE,
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER
    }

    private SeasonsIntegration() {

    }
}
