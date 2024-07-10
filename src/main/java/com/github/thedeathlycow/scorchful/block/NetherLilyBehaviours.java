package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import net.minecraft.world.event.GameEvent;

public class NetherLilyBehaviours {

    public static final NetherLilyBehaviour.NetherLilyBehaviourMap WARPED_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap("scorchful_warped_lily");

    public static final NetherLilyBehaviour.NetherLilyBehaviourMap CRIMSON_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap("scorchful_crimson_lily");

    private static final NetherLilyBehaviour ADD_WATER = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            if (state.get(NetherLilyBlock.WATER_SATURATION_LEVEL) >= NetherLilyBlock.MAX_LEVEL) {
                return ItemActionResult.FAIL;
            }
            Item item = stack.getItem();
            player.incrementStat(SStats.FILL_CRIMSON_LILY);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            NetherLilyBlock.setWater(state, world, pos, 3);
            world.playSound(
                    null,
                    pos,
                    SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS,
                    1.0f, 1.0f
            );
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static void registerBehaviours() {
        WARPED_LILY_BEHAVIOUR.map().put(
                Items.GLASS_BOTTLE,
                (state, world, pos, player, hand, stack) -> {

                    if (state.get(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
                        return ItemActionResult.FAIL;
                    }

                    if (!world.isClient) {
                        Item item = stack.getItem();
                        player.setStackInHand(
                                hand,
                                ItemUsage.exchangeStack(
                                        stack,
                                        player,
                                        PotionContentsComponent.createStack(Items.POTION, Potions.WATER)
                                )
                        );
                        player.incrementStat(SStats.USE_WARPED_LILY);
                        player.incrementStat(Stats.USED.getOrCreateStat(item));
                        NetherLilyBlock.setWater(state, world, pos, 0);
                        world.playSound(
                                null,
                                pos,
                                SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS,
                                1.0f, 1.0f
                        );
                        world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    }
                    return ItemActionResult.success(world.isClient);
                }
        );
        WARPED_LILY_BEHAVIOUR.map().put(SItems.WATER_SKIN, ((WaterSkinItem) SItems.WATER_SKIN)::onWarpedLilyInteract);

        CRIMSON_LILY_BEHAVIOUR.map().put(
                Items.POTION,
                (state, world, pos, player, hand, stack) -> {

                    ItemActionResult result = ADD_WATER.interact(state, world, pos, player, hand, stack);

                    if (!world.isClient && result.isAccepted()) {
                        player.setStackInHand(
                                hand,
                                ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE))
                        );
                    }

                    return result;
                }
        );
        CRIMSON_LILY_BEHAVIOUR.map().put(
                SItems.WATER_SKIN,
                (state, world, pos, player, hand, stack) -> {
                    ItemActionResult result;
                    if (WaterSkinItem.hasDrink(stack)) {
                        result = ADD_WATER.interact(state, world, pos, player, hand, stack);
                    } else {
                        result = ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                    }

                    if (!world.isClient && result.isAccepted()) {
                        WaterSkinItem.addDrinks(stack, -1);
                    }
                    return result;
                }
        );

    }

    private NetherLilyBehaviours() {

    }
}
