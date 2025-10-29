package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.event.GameEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

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

    public static void tickChunk(ServerWorld world, WorldChunk chunk, int randomTickSpeed) {
        // choose position
        final ChunkPos chunkPos = chunk.getPos();
        final BlockPos topPos = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING,
                world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15)
        );

        // control fail conditions
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, topPos);
        if (sandstorm == Sandstorms.SandstormType.NONE) {
            return;
        }
        Random random = world.random;
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
        BlockPos groundPos = topPos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        groundBlock.precipitationTick(groundState, world, groundPos, Biome.Precipitation.NONE);
    }

    public static boolean cauldronSandstormTick(BlockState state, World world, BlockPos pos) {
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, pos.up());

        Block cauldron = SAND_CAULDRONS.get(sandstorm);

        if (cauldron != null) {
            world.setBlockState(pos, cauldron.getDefaultState());
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            return true;
        }

        return false;
    }

    private static void placeSandPile(ServerWorld world, BlockPos topPos, Block sandPileBlock, WeatherConfig config) {

        int accumulationHeight = config.getSandPileAccumulationHeight();
        if (!config.isSandPileAccumulationEnabled() || accumulationHeight <= 0) {
            return;
        }

        if (canSetSand(world, topPos, sandPileBlock)) {
            BlockState sandPileState = sandPileBlock.getDefaultState();
            BlockState currentState = world.getBlockState(topPos);

            if (currentState.isOf(sandPileBlock)) {
                int currentLayers = currentState.get(SandPileBlock.LAYERS);

                if (currentLayers < Math.min(accumulationHeight, SandPileBlock.MAX_LAYERS)) {
                    sandPileState = currentState.with(SnowBlock.LAYERS, currentLayers + 1);
                    Block.pushEntitiesUpBeforeBlockChange(currentState, sandPileState, world, topPos);
                }

            }
            world.setBlockState(topPos, sandPileState);
        }
    }

    private static boolean canSetSand(ServerWorld world, BlockPos pos, Block sandPileBlock) {
        BlockState current = world.getBlockState(pos);
        return pos.getY() >= world.getBottomY()
                && pos.getY() < world.getTopY()
                && (current.isAir() || current.isOf(sandPileBlock))
                && sandPileBlock.getDefaultState().canPlaceAt(world, pos);
    }

    private SandAccumulation() {

    }
}
