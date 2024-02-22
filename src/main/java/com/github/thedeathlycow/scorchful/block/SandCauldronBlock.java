package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.function.Predicate;

public class SandCauldronBlock extends LeveledCauldronBlock {

    public static final Predicate<Sandstorms.SandstormType> REGULAR_SANDSTORM_PREDICATE = sandstorm -> {
        return sandstorm == Sandstorms.SandstormType.REGULAR;
    };

    public static final Predicate<Sandstorms.SandstormType> RED_SANDSTORM_PREDICATE = sandstorm -> {
        return sandstorm == Sandstorms.SandstormType.RED;
    };

    private static final float FILL_WITH_SAND_CHANCE = 0.1f;

    private final Predicate<Sandstorms.SandstormType> sandstormPredicate;

    private final CauldronBehavior filledInteraction;

    /**
     * Constructs a leveled cauldron block.
     *
     * @param settings
     * @param sandstormPredicate a predicate that checks what type of sandstorm can fill this cauldron
     * @param filledInteraction  the behaviour for emptying the cauldron
     * @apiNote The precipitation predicates are compared using identity comparisons in some cases,
     * so callers should typically use {@link #RAIN_PREDICATE} and {@link #SNOW_PREDICATE} if applicable.
     */
    public SandCauldronBlock(
            Settings settings,
            Predicate<Sandstorms.SandstormType> sandstormPredicate,
            CauldronBehavior filledInteraction
    ) {
        super(settings, precipitation -> false, SandCauldronBehaviours.NO_CAULDRON_BEHAVIOURS);
        this.sandstormPredicate = sandstormPredicate;
        this.filledInteraction = filledInteraction;
    }

    public static boolean canFillWithSand(World world, Sandstorms.SandstormType sandstormType) {
        return switch (sandstormType) {
            case RED, REGULAR -> world.getRandom().nextFloat() < FILL_WITH_SAND_CHANCE;
            default -> false;
        };
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        Sandstorms.SandstormType type = Sandstorms.getCurrentSandStorm(world, pos.up());

        if (!this.sandstormPredicate.test(type) || !canFillWithSand(world, type) || state.get(LEVEL) == MAX_LEVEL) {
            return;
        }

        BlockState filled = state.cycle(LEVEL);
        world.setBlockState(pos, filled);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(filled));
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // stub: don't extinguish entities on fire
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (this.isFull(state)) {
            return this.filledInteraction.interact(state, world, pos, player, hand, player.getStackInHand(hand));
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }
}
