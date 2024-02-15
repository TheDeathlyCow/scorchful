package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

        if (!config.isSandPileAccumulationEnabled()) {
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
                Heightmap.Type.WORLD_SURFACE,
                world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15)
        );

        RegistryEntry<Biome> biome = world.getBiome(startPos);

        if (biome.isIn(SBiomeTags.HAS_SAND_STORMS)) {
            placeSand(world, startPos, SBlocks.SAND_PILE);
        } else if (biome.isIn(SBiomeTags.HAS_RED_SAND_STORMS)) {
            placeSand(world, startPos, SBlocks.RED_SAND_PILE);
        }
    }

    private static void placeSand(ServerWorld world, BlockPos startPos, Block sandPileBlock) {
        BlockState sandPile = sandPileBlock.getDefaultState();
        if (canSetSand(world, startPos, sandPile)) {
            world.setBlockState(startPos, sandPileBlock.getDefaultState());
        }
    }

    private static boolean canSetSand(ServerWorld world, BlockPos pos, BlockState sandPileBlockState) {
        BlockState current = world.getBlockState(pos);
        return pos.getY() >= world.getBottomY()
                && pos.getY() < world.getTopY()
                && (current.isAir() || current.isOf(sandPileBlockState.getBlock()))
                && sandPileBlockState.canPlaceAt(world, pos);
    }

    private SandAccumulation() {

    }
}
