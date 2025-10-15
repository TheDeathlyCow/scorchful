package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class SandAccumulation {
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
        Block sandPile = null;
        WeatherConfig config = ScorchfulConfig.getWeatherConfig();
        if (Objects.requireNonNull(sandstorm) == Sandstorms.SandstormType.REGULAR) {
            sandPile = SBlocks.SAND_PILE;
        } else if (sandstorm == Sandstorms.SandstormType.RED) {
            sandPile = SBlocks.RED_SAND_PILE;
        }
        if (sandPile != null) {
            placeSandPile(world, topPos, sandPile, config);
        }

        // cauldron tick
        BlockPos groundPos = topPos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        if (groundBlock instanceof CauldronBlock) {
            tickFillCauldron(world, groundPos);
        }
    }

    private static void tickFillCauldron(World world, BlockPos pos) {
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, pos.up());

        switch (sandstorm) {
            case REGULAR -> {
                world.setBlockState(pos, SBlocks.SAND_CAULDRON.getDefaultState());
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            case RED -> {
                world.setBlockState(pos, SBlocks.RED_SAND_CAULDRON.getDefaultState());
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            default -> {}
        }
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
                && pos.getY() < world.getTopYInclusive()
                && (current.isAir() || current.isOf(sandPileBlock))
                && sandPileBlock.getDefaultState().canPlaceAt(world, pos);
    }

    private SandAccumulation() {

    }
}
