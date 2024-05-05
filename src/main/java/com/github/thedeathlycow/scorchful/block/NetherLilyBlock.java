package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticleEffect;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

@SuppressWarnings("deprecation")
public class NetherLilyBlock extends Block {

    public static final int MIN_LEVEL = 0;

    public static final int MAX_LEVEL = 3;

    public static final IntProperty WATER_SATURATION_LEVEL = IntProperty.of(
            "water_level", MIN_LEVEL, MAX_LEVEL
    );

    private static final VoxelShape SHAPE = Block.createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 3.0, 14.0
    );


    private final NetherLilyBehaviour.NetherLilyBehaviourMap behaviorMap;

    public NetherLilyBlock(Settings settings, NetherLilyBehaviour.NetherLilyBehaviourMap behaviorMap) {
        super(settings);
        this.behaviorMap = behaviorMap;
    }

    public static void setWater(BlockState state, World world, BlockPos pos, int level) {
        BlockState blockState = state.with(WATER_SATURATION_LEVEL, level);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATER_SATURATION_LEVEL);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP) && !world.isWater(pos);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        NetherLilyBehaviour behaviour = this.behaviorMap.map().get(itemStack.getItem());
        return behaviour.interact(state, world, pos, player, hand, itemStack);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(WATER_SATURATION_LEVEL) < 3;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int saturationLevel = state.get(WATER_SATURATION_LEVEL);

        if (saturationLevel != MAX_LEVEL) {
            return;
        }

        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + (random.nextDouble() / 4.0);
        double z = pos.getZ() + random.nextDouble();

        ParticleEffect particle = random.nextFloat() < 0.25f
                ? ParticleTypes.DRIPPING_DRIPSTONE_WATER
                : ParticleTypes.DRIPPING_WATER;

        world.addParticle(
                particle,
                x, y, z,
                0, 0, 0
        );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        if (world.isClient) {
            return;
        }

        DimensionType dimension = world.getDimension();
        if (dimension.ultrawarm()) {
            BlockState below = world.getBlockState(pos.down());
            if (below.isIn(SBlockTags.NETHER_LILY_CAN_ABSORB_WATER)) {
                this.tryGrow(state, world, pos, random);
            }
        } else {
            this.tryGrow(state, world, pos, random);
        }
    }

    private void tryGrow(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int saturation = state.get(WATER_SATURATION_LEVEL);
        if (saturation >= MAX_LEVEL) {
            return;
        }

        Scorchful.LOGGER.debug("Grew a nether lily at {}", pos);
        world.setBlockState(pos, state.with(WATER_SATURATION_LEVEL, saturation + 1), Block.NOTIFY_LISTENERS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
