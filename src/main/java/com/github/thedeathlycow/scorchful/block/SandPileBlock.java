package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SandPileBlock extends FallingBlock {

    public static final MapCodec<SandPileBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT
                                    .fieldOf("color")
                                    .forGetter(b -> b.color),
                            propertiesCodec()
                    )
                    .apply(instance, SandPileBlock::new)
    );

    public static final int MAX_LAYERS = 8;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final VoxelShape[] LAYERS_TO_SHAPE = new VoxelShape[]{
            Shapes.empty(),
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };
    public static final int MAX_PATH_FINDING_LAYERS = 5;
    private final int color;

    public SandPileBlock(int color, Properties settings) {
        super(settings);
        this.color = color;
        this.registerDefaultState(this.defaultBlockState().setValue(LAYERS, 1));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return switch (type) {
            case LAND -> state.getValue(LAYERS) < MAX_PATH_FINDING_LAYERS;
            case WATER, AIR -> false;
        };
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState anchorState = world.getBlockState(pos.below());
        if (anchorState.is(SBlockTags.SAND_PILE_CANNOT_SURVIVE_ON)) {
            return false;
        }
        if (anchorState.is(SBlockTags.SAND_PILE_CAN_SURVIVE_ON)) {
            return true;
        }

        return Block.isFaceFull(anchorState.getCollisionShape(world, pos.below()), Direction.UP)
                || (anchorState.getBlock() instanceof SandPileBlock) && anchorState.getValue(LAYERS) >= MAX_LAYERS;
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!this.canSurvive(state, world, pos)) {
            super.tick(state, world, pos, random);
        }
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LAYERS) == MAX_LAYERS ? 0.2f : 1.0f;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = state.getValue(LAYERS);
        if (context.getItemInHand().is(this.asItem()) && i < MAX_LAYERS) {
            if (context.replacingClickedOnBlock()) {
                return context.getClickedFace() == Direction.UP;
            }
            return true;
        }
        return i == 1;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(MAX_LAYERS, i + 1));
        }
        return super.getStateForPlacement(ctx);
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
        return this.color;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS) - 1];
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LAYERS);
    }
}
