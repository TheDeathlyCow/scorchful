package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.gameevent.GameEvent;

public class NetherLilyBehaviours {
    public static final NetherLilyBehaviour.NetherLilyBehaviourMap WARPED_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap("scorchful_warped_lily");

    public static final NetherLilyBehaviour.NetherLilyBehaviourMap CRIMSON_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap("scorchful_crimson_lily");

    private static final NetherLilyBehaviour ADD_WATER = (state, world, pos, player, hand, stack) -> {
        if (!world.isClientSide()) {
            if (state.getValue(NetherLilyBlock.WATER_SATURATION_LEVEL) >= NetherLilyBlock.MAX_LEVEL) {
                return InteractionResult.FAIL;
            }
            Item item = stack.getItem();
            player.awardStat(SStats.FILL_CRIMSON_LILY);
            player.awardStat(Stats.ITEM_USED.get(item));
            NetherLilyBlock.setWater(state, world, pos, 3);
            world.playSound(
                    null,
                    pos,
                    SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS,
                    1.0f, 1.0f
            );
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.SUCCESS;
    };

    public static void initialize() {
        WARPED_LILY_BEHAVIOUR.map().put(
                Items.GLASS_BOTTLE,
                (state, world, pos, player, hand, stack) -> {

                    if (state.getValue(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
                        return InteractionResult.FAIL;
                    }

                    if (!world.isClientSide()) {
                        Item item = stack.getItem();
                        player.setItemInHand(
                                hand,
                                ItemUtils.createFilledResult(
                                        stack,
                                        player,
                                        PotionContents.createItemStack(Items.POTION, Potions.WATER)
                                )
                        );
                        player.awardStat(SStats.USE_WARPED_LILY);
                        player.awardStat(Stats.ITEM_USED.get(item));
                        NetherLilyBlock.setWater(state, world, pos, 0);
                        world.playSound(
                                null,
                                pos,
                                SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS,
                                1.0f, 1.0f
                        );
                        world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    }
                    return InteractionResult.SUCCESS;
                }
        );
        WARPED_LILY_BEHAVIOUR.map().put(SItems.WATER_SKIN, ((WaterSkinItem) SItems.WATER_SKIN)::onWarpedLilyInteract);

        CRIMSON_LILY_BEHAVIOUR.map().put(
                Items.POTION,
                (state, world, pos, player, hand, stack) -> {

                    InteractionResult result = ADD_WATER.interact(state, world, pos, player, hand, stack);

                    if (!world.isClientSide() && result.consumesAction()) {
                        player.setItemInHand(
                                hand,
                                ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE))
                        );
                    }

                    return result;
                }
        );
        CRIMSON_LILY_BEHAVIOUR.map().put(
                SItems.WATER_SKIN,
                (state, world, pos, player, hand, stack) -> {
                    InteractionResult result;
                    if (WaterSkinItem.hasDrink(stack)) {
                        result = ADD_WATER.interact(state, world, pos, player, hand, stack);
                    } else {
                        result = InteractionResult.TRY_WITH_EMPTY_HAND;
                    }

                    if (!world.isClientSide() && result.consumesAction()) {
                        DrinkContainer.addDrinks(stack, -1);
                    }
                    return result;
                }
        );

    }

    private NetherLilyBehaviours() {

    }
}
