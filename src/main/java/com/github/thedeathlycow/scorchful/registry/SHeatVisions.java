package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.temperature.heatvision.ApproachEffects;
import com.github.thedeathlycow.scorchful.temperature.heatvision.BlockDisplayHeatVision;
import com.github.thedeathlycow.scorchful.temperature.heatvision.EntityHeatVision;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;

public final class SHeatVisions {

    public static final HeatVision HUSK = register(
            "husk",
            new EntityHeatVision<>(
                    SBiomeTags.HEAT_VISION_HUSK,
                    6,
                    EntityType.HUSK
            )
    );
    public static final HeatVision BOAT = register(
            "boat",
            new EntityHeatVision<>(
                    SBiomeTags.HEAT_VISION_BOAT,
                    6,
                    EntityType.BOAT
            )
    );
    public static final HeatVision POPPY = register(
            "poppy",
            new BlockDisplayHeatVision(
                    SBiomeTags.HEAT_VISION_POPPY,
                    3,
                    Blocks.POPPY::getDefaultState
            )
    );
    public static final HeatVision BLUE_ORCHID = register(
            "blue_orchid",
            new BlockDisplayHeatVision(
                    SBiomeTags.HEAT_VISION_BLUE_ORCHID,
                    3,
                    Blocks.BLUE_ORCHID::getDefaultState
            )
    );
    public static final HeatVision SALMON = register(
            "salmon",
            new EntityHeatVision<>(
                    SBiomeTags.HEAT_VISION_SALMON,
                    2,
                    EntityType.SALMON
            )
    );
    public static final HeatVision COD = register(
            "cod",
            new EntityHeatVision<>(
                    SBiomeTags.HEAT_VISION_COD,
                    2,
                    EntityType.COD
            )
    );
    public static final HeatVision SQUID = register(
            "squid",
            new EntityHeatVision<>(
                    SBiomeTags.HEAT_VISION_SQUID,
                    2,
                    EntityType.SQUID
            )
    );


    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful heat visions");
        ApproachEffects.initialize();
    }

    private static HeatVision register(String id, HeatVision vision) {
        return Registry.register(SRegistries.HEAT_VISION, Scorchful.id(id), vision);
    }

    private SHeatVisions() {

    }
}
