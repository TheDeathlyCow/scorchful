package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.mixin.accessor.CauldronInteractionDispatcherAccessor;
import com.github.thedeathlycow.scorchful.mixin.accessor.CauldronInteractionsAccessor;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class SandCauldronInteractions {
    public static final CauldronInteraction.Dispatcher SAND_CAULDRON_BEHAVIOUR = CauldronInteractionsAccessor.scorchful$newDispatcher("scorchful_sand_cauldron");

    public static final CauldronInteraction.Dispatcher RED_SAND_CAULDRON_BEHAVIOUR = CauldronInteractionsAccessor.scorchful$newDispatcher("scorchful_red_sand_cauldron");

    public static void initialize() {
        CauldronInteractionDispatcherAccessor emptyInteractionAccessor = (CauldronInteractionDispatcherAccessor) CauldronInteractions.EMPTY;

        emptyInteractionAccessor.scorchful$put(
                Items.SAND,
                fillWithSand(
                        SBlocks.SAND_CAULDRON.defaultBlockState()
                                .setValue(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        emptyInteractionAccessor.scorchful$put(
                Items.RED_SAND,
                fillWithSand(
                        SBlocks.RED_SAND_CAULDRON.defaultBlockState()
                                .setValue(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
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
     * @return a {@linkplain InteractionResult#consumesAction successful} action result
     */
    public static InteractionResult fillCauldronWithBlock(
            Level world, BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack,
            BlockState state,
            SoundEvent soundEvent
    ) {
        if (!world.isClientSide()) {
            Item item = stack.getItem();
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, state);
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Empties a cauldron if it's full.
     *
     * @param soundEvent the sound produced by emptying
     * @param output     the item stack that replaces the interaction stack when the cauldron is emptied
     * @param stack      the stack in the player's hand
     * @param player     the interacting player
     * @param pos        the cauldron's position
     * @param world      the world where the cauldron is located
     * @param state      the cauldron block state
     * @return a {@linkplain InteractionResult#consumesAction successful} action result if emptied, {@link InteractionResult#PASS} otherwise
     */
    public static InteractionResult emptyBlockFromCauldron(
            BlockState state,
            Level world, BlockPos pos,
            Player player,
            ItemStack stack, ItemStack output,
            SoundEvent soundEvent
    ) {
        if (!world.isClientSide()) {
            if (state.hasProperty(SandCauldronBlock.LEVEL) && state.getValue(SandCauldronBlock.LEVEL) < SandCauldronBlock.MAX_LEVEL) {
                return InteractionResult.FAIL;
            }

            Item item = stack.getItem();

            Inventory inventory = player.getInventory();
            if (!player.isCreative() || !inventory.contains(output)) {
                inventory.add(output);
            }

            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }

        return InteractionResult.SUCCESS;
    }

    private static CauldronInteraction fillWithSand(BlockState filledState) {
        return (state, world, pos, player, hand, stack) -> {
            return fillCauldronWithBlock(
                    world,
                    pos,
                    player,
                    hand,
                    stack,
                    filledState,
                    SoundEvents.SAND_PLACE
            );
        };
    }

    private SandCauldronInteractions() {

    }
}
