package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.temperature.heatvision.BlockDisplayHeatVision;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import com.github.thedeathlycow.scorchful.temperature.heatvision.EntityHeatVision;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;

public class SHeatVisions {

    public static final HeatVision HUSK = new EntityHeatVision<>(
            SBiomeTags.DESERT_VISION_BIOMES,
            EntityType.HUSK
    );
    public static final HeatVision BOAT = new EntityHeatVision<>(
            SBiomeTags.DESERT_VISION_BIOMES,
            EntityType.BOAT
    );
    public static final HeatVision POPPY = new BlockDisplayHeatVision(
            SBiomeTags.DESERT_VISION_BIOMES,
            Blocks.POPPY::getDefaultState
    );


    public static void initialize() {
        register("husk", HUSK);
        register("boat", BOAT);
        register("poppy", POPPY);
    }

    private static void register(String id, HeatVision vision) {
        Registry.register(SRegistries.HEAT_VISION, Scorchful.id(id), vision);
    }

    private SHeatVisions() {

    }
}
