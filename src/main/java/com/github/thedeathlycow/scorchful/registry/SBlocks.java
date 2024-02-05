package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.CrimsonLilyBlock;
import com.github.thedeathlycow.scorchful.block.RootedNyliumBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class SBlocks {

    public static final Block CRIMSON_LILY = new CrimsonLilyBlock(
            FabricBlockSettings.create()
                    .mapColor(MapColor.DARK_RED)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly()
    );

    public static final Block ROOTED_NETHERRACK = new RootedNyliumBlock(
            FabricBlockSettings.copyOf(Blocks.NETHERRACK),
            Blocks.CRIMSON_ROOTS::getDefaultState
    );

    public static final Block ROOTED_CRIMSON_NYLIUM = new RootedNyliumBlock(
            FabricBlockSettings.copyOf(Blocks.CRIMSON_NYLIUM),
            Blocks.CRIMSON_ROOTS::getDefaultState
    );

    public static void registerBlocks() {
        register("crimson_lily", CRIMSON_LILY);
        register("rooted_netherrack", ROOTED_NETHERRACK);
        register("rooted_crimson_nylium", ROOTED_CRIMSON_NYLIUM);
    }

    private static void register(String id, Block block) {
        Registry.register(Registries.BLOCK, Scorchful.id(id), block);
    }

    private SBlocks() {

    }
}
