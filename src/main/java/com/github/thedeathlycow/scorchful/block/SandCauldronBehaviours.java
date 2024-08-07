package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class SandCauldronBehaviours {


    public static final CauldronBehavior.CauldronBehaviorMap SAND_CAULDRON_BEHAVIOUR = CauldronBehavior.createMap("scorchful_sand_cauldron");
    public static final CauldronBehavior.CauldronBehaviorMap RED_SAND_CAULDRON_BEHAVIOUR = CauldronBehavior.createMap("scorchful_red_sand_cauldron");

    public static final CauldronBehavior EMPTY_SAND_CAULDRON = (state, world, pos, player, hand, stack) -> {
        return emptyBlockFromCauldron(
                state,
                world,
                pos,
                player,
                stack,
                Items.SAND.getDefaultStack(),
                SoundEvents.BLOCK_SAND_PLACE
        );
    };

    public static final CauldronBehavior EMPTY_RED_SAND_CAULDRON = (state, world, pos, player, hand, stack) -> {
        return emptyBlockFromCauldron(
                state,
                world,
                pos,
                player,
                stack,
                Items.RED_SAND.getDefaultStack(),
                SoundEvents.BLOCK_SAND_PLACE
        );
    };

    public static void registerAll() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(
                Items.SAND,
                fillWithSand(
                        SBlocks.SAND_CAULDRON.getDefaultState()
                                .with(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(
                Items.RED_SAND,
                fillWithSand(
                        SBlocks.RED_SAND_CAULDRON.getDefaultState()
                                .with(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        if (SAND_CAULDRON_BEHAVIOUR.map() instanceof Object2ObjectOpenHashMap<Item, CauldronBehavior> sandCauldronOpenMap) {
            sandCauldronOpenMap.defaultReturnValue(EMPTY_SAND_CAULDRON);
        } else {
            Scorchful.LOGGER.error("Unable to register default sand cauldron behaviour");
        }

        if (RED_SAND_CAULDRON_BEHAVIOUR.map() instanceof Object2ObjectOpenHashMap<Item, CauldronBehavior> redSandCauldronOpenMap) {
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
     * @return a {@linkplain ActionResult#isAccepted successful} action result
     */
    public static ItemActionResult fillCauldronWithBlock(
            World world, BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack,
            BlockState state,
            SoundEvent soundEvent
    ) {
        if (!world.isClient) {
            Item item = stack.getItem();
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
        return ItemActionResult.success(world.isClient);
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
     * @return a {@linkplain ActionResult#isAccepted successful} action result if emptied, {@link ActionResult#PASS} otherwise
     */
    public static ItemActionResult emptyBlockFromCauldron(
            BlockState state,
            World world, BlockPos pos,
            PlayerEntity player,
            ItemStack stack, ItemStack output,
            SoundEvent soundEvent
    ) {
        if (!world.isClient) {
            if (state.contains(SandCauldronBlock.LEVEL) && state.get(SandCauldronBlock.LEVEL) < SandCauldronBlock.MAX_LEVEL) {
                return ItemActionResult.FAIL;
            }

            Item item = stack.getItem();

            PlayerInventory inventory = player.getInventory();
            if (!player.isCreative() || !inventory.contains(output)) {
                inventory.insertStack(output);
            }

            player.incrementStat(Stats.USE_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
        return ItemActionResult.success(world.isClient);
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

    private SandCauldronBehaviours() {

    }
}
