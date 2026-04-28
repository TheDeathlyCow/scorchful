package com.github.thedeathlycow.scorchful.block;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface NetherLilyBehaviour {

    ItemInteractionResult interact(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack
    );

    Map<String, NetherLilyBehaviourMap> BEHAVIOUR_MAPS = new Object2ObjectArrayMap<>();

    Codec<NetherLilyBehaviourMap> CODEC = Codec.stringResolver(NetherLilyBehaviourMap::name, BEHAVIOUR_MAPS::get);

    static NetherLilyBehaviourMap createMap(String name) {
        var map = new Object2ObjectOpenHashMap<Item, NetherLilyBehaviour>();
        map.defaultReturnValue((state, world, pos, player, hand, stack) -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);

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
