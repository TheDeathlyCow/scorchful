package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.temperature.desertvision.BlockDisplayDesertVisionController;
import com.github.thedeathlycow.scorchful.temperature.desertvision.DesertVisionController;
import com.github.thedeathlycow.scorchful.temperature.desertvision.EntityDesertVisionController;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;

public class SDesertVisionControllers {

    public static final DesertVisionController HUSK = new EntityDesertVisionController<>(
            SBiomeTags.DESERT_VISION_BIOMES,
            EntityType.HUSK
    );
    public static final DesertVisionController BOAT = new EntityDesertVisionController<>(
            SBiomeTags.DESERT_VISION_BIOMES,
            EntityType.BOAT
    );
    public static final DesertVisionController POPPY = new BlockDisplayDesertVisionController(
            SBiomeTags.DESERT_VISION_BIOMES,
            Blocks.POPPY::getDefaultState
    );


    public static void initialize() {
        register("husk", HUSK);
        register("boat", BOAT);
        register("poppy", POPPY);
    }

    private static void register(String id, DesertVisionController vision) {
        Registry.register(SRegistries.DESERT_VISION_CONTROLLERS, Scorchful.id(id), vision);
    }

    private SDesertVisionControllers() {

    }
}
