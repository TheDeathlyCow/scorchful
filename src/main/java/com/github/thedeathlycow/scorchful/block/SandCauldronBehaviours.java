package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class SandCauldronBehaviours {
    public static final CauldronInteraction.InteractionMap SAND_CAULDRON_BEHAVIOUR = CauldronInteraction.newInteractionMap("scorchful_sand_cauldron");
    public static final CauldronInteraction.InteractionMap RED_SAND_CAULDRON_BEHAVIOUR = CauldronInteraction.newInteractionMap("scorchful_red_sand_cauldron");

    public static final CauldronInteraction EMPTY_SAND_CAULDRON = (state, world, pos, player, hand, stack) -> {
        return emptyBlockFromCauldron(
                state,
                world,
                pos,
                player,
                stack,
                Items.SAND.getDefaultInstance(),
                SoundEvents.SAND_PLACE
        );
    };

    public static final CauldronInteraction EMPTY_RED_SAND_CAULDRON = (state, world, pos, player, hand, stack) -> {
        return emptyBlockFromCauldron(
                state,
                world,
                pos,
                player,
                stack,
                Items.RED_SAND.getDefaultInstance(),
                SoundEvents.SAND_PLACE
        );
    };

    public static void initialize() {
        CauldronInteraction.EMPTY.map().put(
                Items.SAND,
                fillWithSand(
                        SBlocks.SAND_CAULDRON.defaultBlockState()
                                .setValue(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        CauldronInteraction.EMPTY.map().put(
                Items.RED_SAND,
                fillWithSand(
                        SBlocks.RED_SAND_CAULDRON.defaultBlockState()
                                .setValue(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        if (SAND_CAULDRON_BEHAVIOUR.map() instanceof Object2ObjectOpenHashMap<Item, CauldronInteraction> sandCauldronOpenMap) {
            sandCauldronOpenMap.defaultReturnValue(EMPTY_SAND_CAULDRON);
        } else {
            Scorchful.LOGGER.error("Unable to register default sand cauldron behaviour");
        }

        if (RED_SAND_CAULDRON_BEHAVIOUR.map() instanceof Object2ObjectOpenHashMap<Item, CauldronInteraction> redSandCauldronOpenMap) {
            redSandCauldronOpenMap.defaultReturnValue(EMPTY_RED_SAND_CAULDRON);
        } else {
            Scorchful.LOGGER.error("Unable to register default red sand cauldron behaviour");
        }
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
    public static ItemInteractionResult fillCauldronWithBlock(
            Level world, BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack,
            BlockState state,
            SoundEvent soundEvent
    ) {
        if (!world.isClientSide) {
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
        return ItemInteractionResult.sidedSuccess(world.isClientSide);
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
    public static ItemInteractionResult emptyBlockFromCauldron(
            BlockState state,
            Level world, BlockPos pos,
            Player player,
            ItemStack stack, ItemStack output,
            SoundEvent soundEvent
    ) {
        if (!world.isClientSide) {
            if (state.hasProperty(SandCauldronBlock.LEVEL) && state.getValue(SandCauldronBlock.LEVEL) < SandCauldronBlock.MAX_LEVEL) {
                return ItemInteractionResult.FAIL;
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
        return ItemInteractionResult.sidedSuccess(world.isClientSide);
    }

    public static CauldronInteraction fillWithSand(BlockState filledState) {
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

    private SandCauldronBehaviours() {

    }
}
