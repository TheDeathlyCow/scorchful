package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;

public class SandAccumulation {


    public static void tickChunk(ServerWorld world, WorldChunk chunk, int randomTickSpeed) {
        WeatherConfig config = Scorchful.getConfig().weatherConfig;
        int accumulationHeight = config.getSandPileAccumulationHeight();

        if (!config.isSandPileAccumulationEnabled() || accumulationHeight <= 0) {
            return;
        }

        if (!world.isRaining()) {
            return;
        }

        Random random = world.random;
        if (random.nextInt(16 * 5) != 0) {
            return;
        }

        final ChunkPos chunkPos = chunk.getPos();
        final BlockPos startPos = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15)
        );

        RegistryEntry<Biome> biome = world.getBiome(startPos);

        if (Sandstorms.hasRegularSandStorms(biome)) {
            placeSand(world, startPos, SBlocks.SAND_PILE, accumulationHeight);
        } else if (Sandstorms.hasRedSandStorms(biome)) {
            placeSand(world, startPos, SBlocks.RED_SAND_PILE, accumulationHeight);
        }
    }

    private static void placeSand(ServerWorld world, BlockPos startPos, Block sandPileBlock, int accumulationHeight) {

        if (canSetSand(world, startPos, sandPileBlock)) {
            BlockState sandPileState = sandPileBlock.getDefaultState();
            BlockState currentState = world.getBlockState(startPos);

            if (currentState.isOf(sandPileBlock)) {
                int currentLayers = currentState.get(SandPileBlock.LAYERS);

                if (currentLayers < Math.min(accumulationHeight, SandPileBlock.MAX_LAYERS)) {
                    sandPileState = currentState.with(SnowBlock.LAYERS, currentLayers + 1);
                    Block.pushEntitiesUpBeforeBlockChange(currentState, sandPileState, world, startPos);
                }

            }
            world.setBlockState(startPos, sandPileState);
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
