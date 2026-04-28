package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public final class SStats {
    public static final ResourceLocation SOAKED_BY_CRIMSON_LILY = register("soaked_by_crimson_lily", StatFormatter.DEFAULT);

    public static final ResourceLocation USE_WARPED_LILY = register("use_warped_lily", StatFormatter.DEFAULT);

    public static final ResourceLocation FILL_CRIMSON_LILY = register("fill_crimson_lily", StatFormatter.DEFAULT);

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful stats");
    }

    private static ResourceLocation register(String name, StatFormatter formatter) {
        ResourceLocation id = Scorchful.id(name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }

    private SStats() {

    }

}
