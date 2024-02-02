package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.CrimsonLilyBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SBlocks {

    public static final Block CRIMSON_LILY = new CrimsonLilyBlock(
            FabricBlockSettings.copyOf(Blocks.SPORE_BLOSSOM)
                    .ticksRandomly()
    );

    public static void registerBlocks() {
        register("crimson_lily", CRIMSON_LILY);
    }

    private static void register(String id, Block block) {
        Registry.register(Registries.BLOCK, Scorchful.id(id), block);
    }

    private SBlocks() {

    }
}
