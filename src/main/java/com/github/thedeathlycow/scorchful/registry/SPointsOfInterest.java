package com.github.thedeathlycow.scorchful.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;

import java.util.Set;
import java.util.stream.Stream;

public class SPointsOfInterest {

    public static final Set<BlockState> SAND_CAULDRONS = Stream.of(
                    SBlocks.SAND_CAULDRON,
                    SBlocks.RED_SAND_CAULDRON
            )
            .flatMap(block -> block.getStateManager().getStates().stream())
            .collect(ImmutableSet.toImmutableSet());


    private SPointsOfInterest() {

    }

}
