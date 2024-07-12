package com.github.thedeathlycow.scorchful.block;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

@FunctionalInterface
public interface NetherLilyBehaviour {

    ItemActionResult interact(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack
    );

    Map<String, NetherLilyBehaviourMap> BEHAVIOUR_MAPS = new Object2ObjectArrayMap<>();

    Codec<NetherLilyBehaviourMap> CODEC = Codec.stringResolver(NetherLilyBehaviourMap::name, BEHAVIOUR_MAPS::get);

    static NetherLilyBehaviourMap createMap(String name) {
        var map = new Object2ObjectOpenHashMap<Item, NetherLilyBehaviour>();
        map.defaultReturnValue((state, world, pos, player, hand, stack) -> ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);

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
