package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public final class SStats {
    public static final Identifier SOAKED_BY_CRIMSON_LILY = register("soaked_by_crimson_lily", StatFormatter.DEFAULT);

    public static final Identifier USE_WARPED_LILY = register("use_warped_lily", StatFormatter.DEFAULT);

    public static final Identifier FILL_CRIMSON_LILY = register("fill_crimson_lily", StatFormatter.DEFAULT);

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful stats");
    }

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier id = Scorchful.id(name);
        Registry.register(Registries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.getOrCreateStat(id, formatter);
        return id;
    }

    private SStats() {

    }

}
