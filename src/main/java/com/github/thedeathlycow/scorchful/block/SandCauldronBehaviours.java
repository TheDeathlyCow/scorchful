package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.SBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SandCauldronBehaviours {

    public static final Map<Item, CauldronBehavior> SAND_CAULDRON_BEHAVIOUR = createSandCauldronMap(Items.SAND::getDefaultStack);

    public static void registerAll() {
        SAND_CAULDRON_BEHAVIOUR.put(
                Items.SAND,
                fillWithSand(
                        SBlocks.SAND_CAULDRON.getDefaultState()
                                .with(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );
    }

    private static CauldronBehavior fillWithSand(BlockState filledState) {
        return (state, world, pos, player, hand, stack) -> {
            return fillCauldronWithBlock(
                    world,
                    pos,
                    player,
                    hand,
                    stack,
                    filledState,
                    SoundEvents.BLOCK_SAND_PLACE
            );
        };
    }

    public static Object2ObjectOpenHashMap<Item, CauldronBehavior> createSandCauldronMap(Supplier<ItemStack> sandStack) {
        return Util.make(
                new Object2ObjectOpenHashMap<>(),
                map -> map.defaultReturnValue(
                        (state, world, pos, player, hand, stack) -> {
                            return CauldronBehavior.emptyCauldron(
                                    state,
                                    world,
                                    pos,
                                    player,
                                    hand,
                                    stack,
                                    sandStack.get(),
                                    s -> s.get(SandCauldronBlock.LEVEL) == SandCauldronBlock.MAX_LEVEL,
                                    SoundEvents.BLOCK_SAND_PLACE
                            );
                        }
                )
        );
    }

    /**
     * Fills a cauldron from a block item stack.
     *
     * <p>The block item  stack will be decremented in the player's
     * inventory.
     *
     * @param pos        the cauldron's position
     * @param world      the world where the cauldron is located
     * @param soundEvent the sound produced by filling
     * @param hand       the hand interacting with the cauldron
     * @param player     the interacting player
     * @param state      the filled cauldron state
     * @param stack      the block item stack in the player's hand
     * @return a {@linkplain ActionResult#isAccepted successful} action result
     */
    public static ActionResult fillCauldronWithBlock(
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack,
            BlockState state,
            SoundEvent soundEvent
    ) {
        if (!world.isClient) {
            Item item = stack.getItem();
            stack.decrement(1);
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
        return ActionResult.success(world.isClient);
    }

    private SandCauldronBehaviours() {

    }
}
