package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public class SandCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<SandCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Sandstorms.SandstormType.CODEC
                                    .fieldOf("sandstorm_type")
                                    .forGetter(block -> block.sandstormType),
                            CauldronInteraction.CODEC
                                    .fieldOf("interactions")
                                    .forGetter(block -> block.interactions),
                            propertiesCodec()
                    )
                    .apply(instance, SandCauldronBlock::new)
    );

    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    private static final int BASE_FLUID_HEIGHT = 6;
    private static final double FLUID_HEIGHT_PER_LEVEL = 3.0;

    private static final float FILL_WITH_SAND_CHANCE = 0.1f;

    private final Sandstorms.SandstormType sandstormType;

    /**
     * Constructs a leveled cauldron block.
     *
     * @param settings
     * @param sandstormType     The type of sandstorm this will fill in
     * @param behaviorMap       other behaviours for this cauldron
     */
    public SandCauldronBlock(
            Sandstorms.SandstormType sandstormType,
            CauldronInteraction.InteractionMap behaviorMap,
            Properties settings
    ) {
        super(settings, behaviorMap);
        this.sandstormType = sandstormType;
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(LEVEL, 3)
        );
    }

    public static boolean canFillWithSand(Level world, Sandstorms.SandstormType sandstormType) {
        return switch (sandstormType) {
            case RED, REGULAR -> world.getRandom().nextFloat() < FILL_WITH_SAND_CHANCE;
            default -> false;
        };
    }

    @Override
    public MapCodec<SandCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return state.getValue(LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEVEL);
    }

    @Override
    public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
        Sandstorms.SandstormType type = Sandstorms.getCurrentSandStorm(world, pos.above());

        if (this.sandstormType != type || !canFillWithSand(world, type) || state.getValue(LEVEL) == MAX_LEVEL) {
            return;
        }

        BlockState filled = state.cycle(LEVEL);
        world.setBlockAndUpdate(pos, filled);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(filled));
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        // stub: don't extinguish entities on fire
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return false;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == MAX_LEVEL;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return (BASE_FLUID_HEIGHT + state.getValue(LEVEL) * FLUID_HEIGHT_PER_LEVEL) / 16.0;
    }
}
