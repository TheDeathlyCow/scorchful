package com.github.thedeathlycow.scorchful.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

@FunctionalInterface
public interface NetherLilyBehaviour {

    ActionResult interact(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack
    );

    Map<String, NetherLilyBehaviourMap> BEHAVIOUR_MAPS = new Object2ObjectArrayMap<>();

    static NetherLilyBehaviourMap createMap(String name) {
        var map = new Object2ObjectOpenHashMap<Item, NetherLilyBehaviour>();
        map.defaultReturnValue((state, world, pos, player, hand, stack) -> ActionResult.PASS);

        var behaviourMap = new NetherLilyBehaviourMap(name, map);
        BEHAVIOUR_MAPS.put(name, behaviourMap);
        return behaviourMap;
    }

    record NetherLilyBehaviourMap(
            String name,
            Map<Item, NetherLilyBehaviour> map
    ) {

    }

}
