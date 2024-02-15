package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
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
                    .ticksRandomly(),
            NetherLilyBehaviours.CRIMSON_LILY_BEHAVIOUR
    );

    public static final Block WARPED_LILY = new NetherLilyBlock(
            FabricBlockSettings.create()
                    .mapColor(MapColor.CYAN)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly(),
            NetherLilyBehaviours.WARPED_LILY_BEHAVIOUR
    );

    public static final Block ROOTED_NETHERRACK = new NetherrackBlock(FabricBlockSettings.copyOf(Blocks.NETHERRACK));

    public static final Block ROOTED_CRIMSON_NYLIUM = new RootedNyliumBlock(
            FabricBlockSettings.copyOf(Blocks.CRIMSON_NYLIUM),
            Blocks.CRIMSON_ROOTS::getDefaultState
    );

    public static final Block ROOTED_WARPED_NYLIUM = new RootedNyliumBlock(
            FabricBlockSettings.copyOf(Blocks.WARPED_NYLIUM),
            Blocks.WARPED_ROOTS::getDefaultState
    );

    public static final Block SAND_PILE = new SandPileBlock(
            FabricBlockSettings.copyOf(Blocks.SAND)
                    .replaceable()
                    .notSolid()
                    .requiresTool()
                    .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= 8)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static void registerBlocks() {
        register("crimson_lily", CRIMSON_LILY);
        register("warped_lily", WARPED_LILY);
        register("rooted_netherrack", ROOTED_NETHERRACK);
        register("rooted_crimson_nylium", ROOTED_CRIMSON_NYLIUM);
        register("rooted_warped_nylium", ROOTED_WARPED_NYLIUM);
        register("sand_pile", SAND_PILE);
    }

    private static void register(String id, Block block) {
        Registry.register(Registries.BLOCK, Scorchful.id(id), block);
    }

    private SBlocks() {

    }
}
