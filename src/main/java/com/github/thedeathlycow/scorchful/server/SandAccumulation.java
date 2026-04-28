package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.google.common.base.Suppliers;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;

public class SandAccumulation {
    public static final Map<Sandstorms.SandstormType, Block> SAND_PILES = Util.make(
            () -> {
                Map<Sandstorms.SandstormType, Block> map = new EnumMap<>(Sandstorms.SandstormType.class);
                map.put(Sandstorms.SandstormType.REGULAR, SBlocks.SAND_PILE);
                map.put(Sandstorms.SandstormType.RED, SBlocks.RED_SAND_PILE);
                return map;
            }
    );

    public static final Map<Sandstorms.SandstormType, Block> SAND_CAULDRONS = Util.make(
            () -> {
                Map<Sandstorms.SandstormType, Block> map = new EnumMap<>(Sandstorms.SandstormType.class);
                map.put(Sandstorms.SandstormType.REGULAR, SBlocks.SAND_CAULDRON);
                map.put(Sandstorms.SandstormType.RED, SBlocks.RED_SAND_CAULDRON);
                return map;
            }
    );

    public static void tickChunk(ServerLevel world, LevelChunk chunk, int randomTickSpeed) {
        // choose position
        final ChunkPos chunkPos = chunk.getPos();
        final BlockPos topPos = world.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING,
                world.getBlockRandomPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), 15)
        );

        // control fail conditions
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, topPos);
        if (sandstorm == Sandstorms.SandstormType.NONE) {
            return;
        }
        RandomSource random = world.random;
        if (random.nextInt(16) != 0) {
            return;
        }

        // sand pile placement
        Block sandPile = SAND_PILES.get(sandstorm);
        if (sandPile == null) {
            return;
        }

        WeatherConfig config = Scorchful.getConfig().weatherConfig;

        placeSandPile(world, topPos, sandPile, config);

        // cauldron tick
        BlockPos groundPos = topPos.below();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        groundBlock.handlePrecipitation(groundState, world, groundPos, Biome.Precipitation.NONE);
    }

    public static boolean cauldronSandstormTick(BlockState state, Level world, BlockPos pos) {
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, pos.above());

        Block cauldron = SAND_CAULDRONS.get(sandstorm);

        if (cauldron != null) {
            world.setBlockAndUpdate(pos, cauldron.defaultBlockState());
            world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            return true;
        }

        return false;
    }

    private static void placeSandPile(ServerLevel world, BlockPos topPos, Block sandPileBlock, WeatherConfig config) {

        int accumulationHeight = config.getSandPileAccumulationHeight();
        if (!config.isSandPileAccumulationEnabled() || accumulationHeight <= 0) {
            return;
        }

        if (canSetSand(world, topPos, sandPileBlock)) {
            BlockState sandPileState = sandPileBlock.defaultBlockState();
            BlockState currentState = world.getBlockState(topPos);

            if (currentState.is(sandPileBlock)) {
                int currentLayers = currentState.getValue(SandPileBlock.LAYERS);

                if (currentLayers < Math.min(accumulationHeight, SandPileBlock.MAX_LAYERS)) {
                    sandPileState = currentState.setValue(SnowLayerBlock.LAYERS, currentLayers + 1);
                    Block.pushEntitiesUp(currentState, sandPileState, world, topPos);
                }

            }
            world.setBlockAndUpdate(topPos, sandPileState);
        }
    }

    private static boolean canSetSand(ServerLevel world, BlockPos pos, Block sandPileBlock) {
        BlockState current = world.getBlockState(pos);
        return pos.getY() >= world.getMinBuildHeight()
                && pos.getY() < world.getMaxBuildHeight()
                && (current.isAir() || current.is(sandPileBlock))
                && sandPileBlock.defaultBlockState().canSurvive(world, pos);
    }

    private SandAccumulation() {

    }
}
