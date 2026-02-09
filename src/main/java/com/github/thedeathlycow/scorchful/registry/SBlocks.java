package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.*;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public final class SBlocks {
    public static final Block CRIMSON_LILY = register(
            "crimson_lily",
            settings -> new CrimsonLilyBlock(
                    NetherLilyBehaviours.CRIMSON_LILY_BEHAVIOUR,
                    settings
                            .mapColor(MapColor.NETHER)
                            .instabreak()
                            .noCollision()
                            .sound(SoundType.WEEPING_VINES)
                            .pushReaction(PushReaction.DESTROY)
                            .randomTicks()
            )
    );

    public static final Block WARPED_LILY = register(
            "warped_lily",
            settings -> new NetherLilyBlock(
                    NetherLilyBehaviours.WARPED_LILY_BEHAVIOUR,
                    settings
                            .mapColor(MapColor.COLOR_CYAN)
                            .instabreak()
                            .noCollision()
                            .sound(SoundType.WEEPING_VINES)
                            .pushReaction(PushReaction.DESTROY)
                            .randomTicks()
            )
    );

    public static final Block ROOTED_NETHERRACK = register(
            "rooted_netherrack",
            settings -> new NetherrackBlock(settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK)
    );

    public static final Block ROOTED_CRIMSON_NYLIUM = register(
            "rooted_crimson_nylium",
            settings -> new RootedNyliumBlock(
                    Blocks.CRIMSON_ROOTS,
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_NYLIUM)
    );

    public static final Block ROOTED_WARPED_NYLIUM = register(
            "rooted_warped_nylium",
            settings -> new RootedNyliumBlock(
                    Blocks.WARPED_ROOTS,
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_NYLIUM)
    );

    public static final Block SAND_PILE = register(
            "sand_pile",
            settings -> new SandPileBlock(
                    0xDBD3A0,
                    settings
                            .replaceable()
                            .forceSolidOff()
                            .isViewBlocking((state, world, pos) -> state.getValue(SnowLayerBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                            .pushReaction(PushReaction.DESTROY)
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
    );

    public static final Block RED_SAND_PILE = register(
            "red_sand_pile",
            settings -> new SandPileBlock(
                    0xA95821,
                    settings
                            .replaceable()
                            .forceSolidOff()
                            .isViewBlocking((state, world, pos) -> state.getValue(SnowLayerBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                            .pushReaction(PushReaction.DESTROY)
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND)
    );

    public static final Block SAND_CAULDRON = register(
            "sand_cauldron",
            settings -> new SandCauldronBlock(
                    Sandstorms.SandstormType.REGULAR,
                    SandCauldronBehaviours.SAND_CAULDRON_BEHAVIOUR,
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
    );

    public static final Block RED_SAND_CAULDRON = register(
            "red_sand_cauldron",
            settings -> new SandCauldronBlock(
                    Sandstorms.SandstormType.RED,
                    SandCauldronBehaviours.RED_SAND_CAULDRON_BEHAVIOUR,
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful blocks");
        SandCauldronBehaviours.initialize();
        NetherLilyBehaviours.initialize();
    }

    private static Block register(String id, Function<BlockBehaviour.Properties, Block> blockFactory) {
        return register(id, blockFactory, BlockBehaviour.Properties.of());
    }

    private static Block register(String id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Scorchful.id(id));
        Block block = blockFactory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private SBlocks() {

    }
}
