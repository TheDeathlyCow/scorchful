package com.github.thedeathlycow.scorchful.block.mirage;

import com.github.thedeathlycow.scorchful.registry.SBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MirageBlockEntity extends BlockEntity {
    public MirageBlockEntity(BlockPos pos, BlockState state) {
        super(SBlockEntityTypes.MIRAGE, pos, state);
    }
}
