package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class SStats {

    public static final Identifier SOAKED_BY_CRIMSON_LILY = Scorchful.id("soaked_by_crimson_lily");

    public static final Identifier USE_WARPED_LILY = Scorchful.id("use_warped_lily");

    public static final Identifier FILL_CRIMSON_LILY = Scorchful.id("fill_crimson_lily");

    public static void registerAll() {
        register(SOAKED_BY_CRIMSON_LILY, StatFormatter.DEFAULT);
        register(USE_WARPED_LILY, StatFormatter.DEFAULT);
        register(FILL_CRIMSON_LILY, StatFormatter.DEFAULT);
    }

    private static void register(Identifier identifier, StatFormatter formatter) {
        Registry.register(Registries.CUSTOM_STAT, identifier, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
    }

    private SStats() {

    }

}
