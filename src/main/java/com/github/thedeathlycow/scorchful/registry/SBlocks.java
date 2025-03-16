package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.*;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public final class SBlocks {
    public static final Block CRIMSON_LILY = register(
            "crimson_lily",
            settings ->  new CrimsonLilyBlock(
                    NetherLilyBehaviours.CRIMSON_LILY_BEHAVIOUR,
                    settings
                            .mapColor(MapColor.DARK_RED)
                            .breakInstantly()
                            .noCollision()
                            .sounds(BlockSoundGroup.WEEPING_VINES)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .ticksRandomly()
            )
    );

    public static final Block WARPED_LILY = register(
            "warped_lily",
            settings -> new NetherLilyBlock(
                    NetherLilyBehaviours.WARPED_LILY_BEHAVIOUR,
                    settings
                            .mapColor(MapColor.CYAN)
                            .breakInstantly()
                            .noCollision()
                            .sounds(BlockSoundGroup.WEEPING_VINES)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .ticksRandomly()
            )
    );

    public static final Block ROOTED_NETHERRACK = register(
            "rooted_netherrack",
            settings -> new NetherrackBlock(settings),
            AbstractBlock.Settings.copy(Blocks.NETHERRACK)
    );

    public static final Block ROOTED_CRIMSON_NYLIUM = register(
            "rooted_crimson_nylium",
            settings -> new RootedNyliumBlock(
                    Blocks.CRIMSON_ROOTS,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.CRIMSON_NYLIUM)
    );

    public static final Block ROOTED_WARPED_NYLIUM = register(
            "rooted_warped_nylium",
            settings -> new RootedNyliumBlock(
                    Blocks.WARPED_ROOTS,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.WARPED_NYLIUM)
    );

    public static final Block SAND_PILE = register(
            "sand_pile",
            settings -> new SandPileBlock(
                    0xDBD3A0,
                    settings
                            .replaceable()
                            .notSolid()
                            .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                            .pistonBehavior(PistonBehavior.DESTROY)
            ),
            AbstractBlock.Settings.copy(Blocks.SAND)
    );

    public static final Block RED_SAND_PILE = register(
            "red_sand_pile",
            settings -> new SandPileBlock(
                    0xA95821,
                    settings
                            .replaceable()
                            .notSolid()
                            .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                            .pistonBehavior(PistonBehavior.DESTROY)
            ),
            AbstractBlock.Settings.copy(Blocks.RED_SAND)
    );

    public static final Block SAND_CAULDRON = register(
            "sand_cauldron",
            settings -> new SandCauldronBlock(
                    Sandstorms.SandstormType.REGULAR,
                    SandCauldronBehaviours.SAND_CAULDRON_BEHAVIOUR,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.CAULDRON)
    );

    public static final Block RED_SAND_CAULDRON = register(
            "red_sand_cauldron",
            settings -> new SandCauldronBlock(
                    Sandstorms.SandstormType.RED,
                    SandCauldronBehaviours.RED_SAND_CAULDRON_BEHAVIOUR,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.CAULDRON)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful blocks");
        SandCauldronBehaviours.initialize();
        NetherLilyBehaviours.initialize();
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> blockFactory) {
        return register(id, blockFactory, AbstractBlock.Settings.create());
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        Block block = blockFactory.apply(settings);
        return Registry.register(Registries.BLOCK, Scorchful.id(id), block);
    }

    private SBlocks() {

    }
}
