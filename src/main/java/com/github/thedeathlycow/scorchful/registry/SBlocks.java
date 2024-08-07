package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.*;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class SBlocks {

    public static final Block CRIMSON_LILY = new CrimsonLilyBlock(
            NetherLilyBehaviours.CRIMSON_LILY_BEHAVIOUR,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly()
    );

    public static final Block WARPED_LILY = new NetherLilyBlock(
            NetherLilyBehaviours.WARPED_LILY_BEHAVIOUR,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .breakInstantly()
                    .noCollision()
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly()
    );

    public static final Block ROOTED_NETHERRACK = new NetherrackBlock(AbstractBlock.Settings.copy(Blocks.NETHERRACK));

    public static final Block ROOTED_CRIMSON_NYLIUM = new RootedNyliumBlock(
            Blocks.CRIMSON_ROOTS,
            AbstractBlock.Settings.copy(Blocks.CRIMSON_NYLIUM)
    );

    public static final Block ROOTED_WARPED_NYLIUM = new RootedNyliumBlock(
            Blocks.WARPED_ROOTS,
            AbstractBlock.Settings.copy(Blocks.WARPED_NYLIUM)
    );

    public static final Block SAND_PILE = new SandPileBlock(
            0xDBD3A0,
            AbstractBlock.Settings.copy(Blocks.SAND)
                    .replaceable()
                    .notSolid()
                    .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block RED_SAND_PILE = new SandPileBlock(
            0xA95821,
            AbstractBlock.Settings.copy(Blocks.RED_SAND)
                    .replaceable()
                    .notSolid()
                    .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block SAND_CAULDRON = new SandCauldronBlock(
            Sandstorms.SandstormType.REGULAR,
            SandCauldronBehaviours.SAND_CAULDRON_BEHAVIOUR,
            AbstractBlock.Settings.copy(Blocks.CAULDRON)
    );

    public static final Block RED_SAND_CAULDRON = new SandCauldronBlock(
            Sandstorms.SandstormType.RED,
            SandCauldronBehaviours.RED_SAND_CAULDRON_BEHAVIOUR,
            AbstractBlock.Settings.copy(Blocks.CAULDRON)
    );

    public static void registerBlocks() {
        register("crimson_lily", CRIMSON_LILY);
        register("warped_lily", WARPED_LILY);
        register("rooted_netherrack", ROOTED_NETHERRACK);
        register("rooted_crimson_nylium", ROOTED_CRIMSON_NYLIUM);
        register("rooted_warped_nylium", ROOTED_WARPED_NYLIUM);
        register("sand_pile", SAND_PILE);
        register("red_sand_pile", RED_SAND_PILE);
        register("sand_cauldron", SAND_CAULDRON);
        register("red_sand_cauldron", RED_SAND_CAULDRON);
    }

    private static void register(String id, Block block) {
        Registry.register(Registries.BLOCK, Scorchful.id(id), block);
    }

    private SBlocks() {

    }
}
