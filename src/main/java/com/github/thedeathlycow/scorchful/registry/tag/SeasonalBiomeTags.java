package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public record SeasonalBiomeTags(
        TagKey<Biome> scorching,
        TagKey<Biome> warm
) {

    private static final SeasonalBiomeTags SPRING_TAGS = new SeasonalBiomeTags(
            SBiomeTags.register("temperature/spring/scorching"),
            SBiomeTags.register("temperature/spring/warm")
    );
    private static final SeasonalBiomeTags SUMMER_TAGS = new SeasonalBiomeTags(
            SBiomeTags.register("temperature/summer/scorching"),
            SBiomeTags.register("temperature/summer/warm")
    );
    private static final SeasonalBiomeTags AUTUMN_TAGS = new SeasonalBiomeTags(
            SBiomeTags.register("temperature/autumn/scorching"),
            SBiomeTags.register("temperature/autumn/warm")
    );
    private static final SeasonalBiomeTags WINTER_TAGS = new SeasonalBiomeTags(
            SBiomeTags.register("temperature/winter/scorching"),
            SBiomeTags.register("temperature/winter/warm")
    );

    /**
     * Returns the biome tags for each season. If no season is provided, then returns the tags for spring
     *
     * @param season The season
     * @return The tags of that season, or spring if no season is given
     */
    public static SeasonalBiomeTags forSeason(ThermooSeason season) {
        return switch (season) {
            case SUMMER -> SUMMER_TAGS;
            case WINTER -> WINTER_TAGS;
            case AUTUMN -> AUTUMN_TAGS;
            case null, default -> SPRING_TAGS;
        };
    }
}
