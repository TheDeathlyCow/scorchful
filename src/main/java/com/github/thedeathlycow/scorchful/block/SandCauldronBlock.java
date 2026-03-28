package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class SandCauldronBlock extends AbstractCauldronBlock {
    public static final MapCodec<SandCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Sandstorms.SandstormType.CODEC
                                    .fieldOf("sandstorm_type")
                                    .forGetter(block -> block.sandstormType),
                            CauldronInteractions.CODEC
                                    .fieldOf("interactions")
                                    .forGetter(block -> block.interactions),
                            ItemStackTemplate.CODEC
                                    .fieldOf("returned_item")
                                    .forGetter(block -> block.returnedItem.get()),
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
    private final Supplier<ItemStackTemplate> returnedItem;

    public SandCauldronBlock(
            Sandstorms.SandstormType sandstormType,
            CauldronInteraction.Dispatcher behaviorMap,
            Supplier<ItemStackTemplate> returnedItem,
            Properties settings
    ) {
        super(settings, behaviorMap);
        this.sandstormType = sandstormType;
        this.returnedItem = returnedItem;
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(LEVEL, 3)
        );
    }

    private SandCauldronBlock(
            Sandstorms.SandstormType sandstormType,
            CauldronInteraction.Dispatcher behaviorMap,
            ItemStackTemplate returnedItem,
            Properties settings
    ) {
        this(
                sandstormType,
                behaviorMap,
                () -> returnedItem,
                settings
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
    protected InteractionResult useItemOn(
            final ItemStack itemStack,
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Player player,
            final InteractionHand hand,
            final BlockHitResult hitResult
    ) {
        InteractionResult result = super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);

        if (result == InteractionResult.TRY_WITH_EMPTY_HAND) {
            return SandCauldronInteractions.emptyBlockFromCauldron(
                    state,
                    level,
                    pos,
                    player,
                    itemStack,
                    returnedItem.get().create(),
                    SoundEvents.SAND_PLACE
            );
        }

        return result;
    }

    @Override
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
