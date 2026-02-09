package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.mixin.accessor.BlockDisplayAccessor;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class BlockDisplayHeatVision extends EntityHeatVision<Display.BlockDisplay> {

    private final Supplier<BlockState> blockStateProvider;

    public BlockDisplayHeatVision(TagKey<Biome> allowedBiomes, int weight, Supplier<BlockState> blockStateProvider) {
        super(allowedBiomes, weight, EntityType.BLOCK_DISPLAY);
        this.blockStateProvider = blockStateProvider;
    }

    @Override
    public boolean spawn(Player player, ServerLevel world, BlockPos pos) {
        if (world.getBlockState(pos).canBeReplaced()) {
            return super.spawn(player, world, pos);
        }
        return false;
    }

    @Override
    protected void initializeEntity(Display.BlockDisplay entity) {
        super.initializeEntity(entity);
        ((BlockDisplayAccessor) entity).scorchful$setBlockState(blockStateProvider.get());
    }
}
