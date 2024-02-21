package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class NetherLilyBehaviours {

    public static final Map<Item, NetherLilyBehaviour> WARPED_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap();

    public static final Map<Item, NetherLilyBehaviour> CRIMSON_LILY_BEHAVIOUR = NetherLilyBehaviour.createMap();

    private static final NetherLilyBehaviour ADD_WATER = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
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
        return ActionResult.success(world.isClient);
    };

    public static void registerBehaviours() {
        WARPED_LILY_BEHAVIOUR.put(
                Items.GLASS_BOTTLE,
                (state, world, pos, player, hand, stack) -> {

                    if (state.get(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
                        return ActionResult.FAIL;
                    }

                    if (!world.isClient) {
                        Item item = stack.getItem();
                        player.setStackInHand(
                                hand,
                                ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER))
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
                    return ActionResult.success(world.isClient);
                }
        );
        WARPED_LILY_BEHAVIOUR.put(SItems.WATER_SKIN, ((WaterSkinItem) SItems.WATER_SKIN)::onWarpedLilyInteract);

        CRIMSON_LILY_BEHAVIOUR.put(
                Items.POTION,
                (state, world, pos, player, hand, stack) -> {

                    ActionResult result = ADD_WATER.interact(state, world, pos, player, hand, stack);

                    if (!world.isClient && result.isAccepted()) {
                        player.setStackInHand(
                                hand,
                                ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE))
                        );
                    }

                    return result;
                }
        );
        CRIMSON_LILY_BEHAVIOUR.put(
                SItems.WATER_SKIN,
                (state, world, pos, player, hand, stack) -> {

                    WaterSkinItem waterSkinItem = (WaterSkinItem) stack.getItem();

                    ActionResult result;
                    if (WaterSkinItem.hasDrink(stack)) {
                        result = ADD_WATER.interact(state, world, pos, player, hand, stack);
                    } else {
                        result = ActionResult.PASS;
                    }

                    if (!world.isClient && result.isAccepted()) {
                        waterSkinItem.addDrinks(stack, -1);
                    }

                    return result;
                }
        );

    }

    private NetherLilyBehaviours() {

    }
}
