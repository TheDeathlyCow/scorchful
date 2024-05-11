package com.github.thedeathlycow.scorchful.block.mirage;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MirageBlock extends BlockWithEntity {

    public static final MapCodec<MirageBlock> CODEC = createCodec(MirageBlock::new);

    public MirageBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
