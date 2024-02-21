package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
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

    /**
     * Constructs a leveled cauldron block.
     *
     * @param settings
     * @param sandstormPredicate a predicate that checks what type of sandstorm can fill this cauldron
     * @param behaviorMap        the map containing cauldron behaviors for each item
     * @apiNote The precipitation predicates are compared using identity comparisons in some cases,
     * so callers should typically use {@link #RAIN_PREDICATE} and {@link #SNOW_PREDICATE} if applicable.
     */
    public SandCauldronBlock(
            Settings settings,
            Predicate<Sandstorms.SandstormType> sandstormPredicate,
            Map<Item, CauldronBehavior> behaviorMap
    ) {
        super(settings, precipitation -> false, behaviorMap);
        this.sandstormPredicate = sandstormPredicate;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // stub: don't extinguish entities on fire
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if (world.getRandom().nextFloat() >= FILL_WITH_SAND_CHANCE || state.get(LEVEL) == MAX_LEVEL) {
            return;
        }

        Sandstorms.SandstormType type = Sandstorms.getCurrentSandStorm(world, pos);
        if (!this.sandstormPredicate.test(type)) {
            return;
        }

        BlockState filled = state.cycle(LEVEL);
        world.setBlockState(pos, filled);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(filled));
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }
}
