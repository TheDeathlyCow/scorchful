package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.function.Predicate;


public class SandCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<SandCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Sandstorms.SandstormType.CODEC
                                    .fieldOf("sandstorm_type")
                                    .forGetter(block -> block.sandstormType),
                            CauldronBehavior.CODEC
                                    .fieldOf("interactions")
                                    .forGetter(block -> block.behaviorMap),
                            createSettingsCodec()
                    )
                    .apply(instance, SandCauldronBlock::new)
    );

    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    private static final int BASE_FLUID_HEIGHT = 6;
    private static final double FLUID_HEIGHT_PER_LEVEL = 3.0;

    private static final float FILL_WITH_SAND_CHANCE = 0.1f;

    private final Sandstorms.SandstormType sandstormType;

    /**
     * Constructs a leveled cauldron block.
     *
     * @param settings
     * @param sandstormType     The type of sandstorm this will fill in
     * @param filledInteraction the behaviour for emptying the cauldron
     * @param behaviorMap       other behaviours for this cauldron
     */
    public SandCauldronBlock(
            Sandstorms.SandstormType sandstormType,
            CauldronBehavior.CauldronBehaviorMap behaviorMap,
            Settings settings
    ) {
        super(settings, behaviorMap);
        this.sandstormType = sandstormType;
    }

    public static boolean canFillWithSand(World world, Sandstorms.SandstormType sandstormType) {
        return switch (sandstormType) {
            case RED, REGULAR -> world.getRandom().nextFloat() < FILL_WITH_SAND_CHANCE;
            default -> false;
        };
    }

    @Override
    public MapCodec<SandCauldronBlock> getCodec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        Sandstorms.SandstormType type = Sandstorms.getCurrentSandStorm(world, pos.up());

        if (this.sandstormType != type || !canFillWithSand(world, type) || state.get(LEVEL) == MAX_LEVEL) {
            return;
        }

        BlockState filled = state.cycle(LEVEL);
        world.setBlockState(pos, filled);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(filled));
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // stub: don't extinguish entities on fire
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == MAX_LEVEL;
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return (BASE_FLUID_HEIGHT + state.get(LEVEL) * FLUID_HEIGHT_PER_LEVEL) / 16.0;
    }
}
