package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class SandAccumulation {


    public static void tickChunk(ServerWorld world, WorldChunk chunk, int randomTickSpeed) {
        final ChunkPos chunkPos = chunk.getPos();
        final BlockPos topPos = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING,
                world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15)
        );

        WeatherConfig config = Scorchful.getConfig().weatherConfig;

        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, topPos);

        if (sandstorm == Sandstorms.SandstormType.NONE) {
            return;
        }

        Random random = world.random;
        if (random.nextInt(16) != 0) {
            return;
        }

        Block sandPile = null;
        if (Objects.requireNonNull(sandstorm) == Sandstorms.SandstormType.REGULAR) {
            sandPile = SBlocks.SAND_PILE;
        } else if (sandstorm == Sandstorms.SandstormType.RED) {
            sandPile = SBlocks.RED_SAND_PILE;
        }

        placeSandPile(world, topPos, sandPile, config);

        BlockPos groundPos = topPos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        groundBlock.precipitationTick(groundState, world, groundPos, Biome.Precipitation.NONE);
    }

    public static boolean cauldronSandstormTick(BlockState state, World world, BlockPos pos) {
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, pos);

        return switch (sandstorm) {
            case REGULAR -> {
                world.setBlockState(pos, SBlocks.SAND_CAULDRON.getDefaultState());
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                yield true;
            }
            case RED -> {
                world.setBlockState(pos, SBlocks.RED_SAND_CAULDRON.getDefaultState());
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                yield true;
            }
            default -> false;
        };
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
