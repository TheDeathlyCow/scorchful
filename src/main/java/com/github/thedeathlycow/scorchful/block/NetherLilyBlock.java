package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SBlockProperties;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetherLilyBlock extends Block {

    public static final MapCodec<NetherLilyBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            NetherLilyBehaviour.CODEC
                                    .fieldOf("behaviors")
                                    .forGetter(block -> block.behaviorMap),
                            propertiesCodec()
                    )
                    .apply(instance, NetherLilyBlock::new)
    );

    public static final int MIN_LEVEL = 0;

    public static final int MAX_LEVEL = 3;

    public static final IntegerProperty WATER_SATURATION_LEVEL = SBlockProperties.WATER_LEVEL_0_3;

    private static final VoxelShape SHAPE = Block.box(
            2.0, 0.0, 2.0,
            14.0, 3.0, 14.0
    );

    private final NetherLilyBehaviour.NetherLilyBehaviourMap behaviorMap;

    public NetherLilyBlock(NetherLilyBehaviour.NetherLilyBehaviourMap behaviorMap, Properties settings) {
        super(settings);
        this.behaviorMap = behaviorMap;
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(WATER_SATURATION_LEVEL, 0)
        );
    }

    public static void setWater(BlockState state, Level world, BlockPos pos, int level) {
        BlockState blockState = state.setValue(WATER_SATURATION_LEVEL, level);
        world.setBlockAndUpdate(pos, blockState);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
    }

    @Override
    protected MapCodec<? extends NetherLilyBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATER_SATURATION_LEVEL);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return Block.canSupportCenter(world, pos.below(), Direction.UP) && !world.isWaterAt(pos);
    }

    @Override
    public BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState,
            LevelAccessor world,
            BlockPos pos, BlockPos neighborPos
    ) {
        if (direction == Direction.DOWN && !this.canSurvive(state, world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state,
            Level world, BlockPos pos,
            Player player, InteractionHand hand,
            BlockHitResult hit
    ) {
        NetherLilyBehaviour behaviour = this.behaviorMap.map().get(stack.getItem());
        return behaviour.interact(state, world, pos, player, hand, stack);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(WATER_SATURATION_LEVEL) < 3;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int saturationLevel = state.getValue(WATER_SATURATION_LEVEL);

        if (saturationLevel != MAX_LEVEL) {
            return;
        }

        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + (random.nextDouble() / 4.0);
        double z = pos.getZ() + random.nextDouble();

        ParticleOptions particle = random.nextFloat() < 0.25f
                ? ParticleTypes.DRIPPING_DRIPSTONE_WATER
                : ParticleTypes.DRIPPING_WATER;

        world.addParticle(
                particle,
                x, y, z,
                0, 0, 0
        );
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {

        if (world.isClientSide) {
            return;
        }

        DimensionType dimension = world.dimensionType();
        if (dimension.ultraWarm()) {
            BlockState below = world.getBlockState(pos.below());
            if (below.is(SBlockTags.NETHER_LILY_CAN_ABSORB_WATER)) {
                this.tryGrow(state, world, pos, random);
            }
        } else {
            this.tryGrow(state, world, pos, random);
        }
    }

    private void tryGrow(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int saturation = state.getValue(WATER_SATURATION_LEVEL);
        if (saturation >= MAX_LEVEL) {
            return;
        }

        Scorchful.LOGGER.debug("Grew a nether lily at {}", pos);
        world.setBlock(pos, state.setValue(WATER_SATURATION_LEVEL, saturation + 1), Block.UPDATE_CLIENTS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
