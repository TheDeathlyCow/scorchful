package com.github.thedeathlycow.scorchful.world;

import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Objects;

public class SandAccumulation {
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
        RandomSource random = world.getRandom();
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
        BlockPos groundPos = topPos.below();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        if (groundBlock instanceof CauldronBlock) {
            tickFillCauldron(world, groundPos);
        }
    }

    private static void tickFillCauldron(Level world, BlockPos pos) {
        Sandstorms.SandstormType sandstorm = Sandstorms.getCurrentSandStorm(world, pos.above());

        switch (sandstorm) {
            case REGULAR -> {
                world.setBlockAndUpdate(pos, SBlocks.SAND_CAULDRON.defaultBlockState());
                world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            case RED -> {
                world.setBlockAndUpdate(pos, SBlocks.RED_SAND_CAULDRON.defaultBlockState());
                world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            default -> {}
        }
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
        return pos.getY() >= world.getMinY()
                && pos.getY() < world.getMaxY()
                && (current.isAir() || current.is(sandPileBlock))
                && sandPileBlock.defaultBlockState().canSurvive(world, pos);
    }

    private SandAccumulation() {

    }
}
