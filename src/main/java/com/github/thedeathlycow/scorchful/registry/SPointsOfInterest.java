package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.mixin.accessor.PointOfInterestTypeAccessor;
import com.github.thedeathlycow.scorchful.mixin.accessor.PointOfInterestTypesAccessor;
import com.google.common.collect.ImmutableSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;

public final class SPointsOfInterest {
    public static final Set<BlockState> SAND_CAULDRONS = Stream.of(
                    SBlocks.SAND_CAULDRON,
                    SBlocks.RED_SAND_CAULDRON
            )
            .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
            .collect(ImmutableSet.toImmutableSet());

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful POIs");
        Holder<PoiType> leatherWorkerPOI = BuiltInRegistries.POINT_OF_INTEREST_TYPE
                .getHolder(PoiTypes.LEATHERWORKER)
                .orElseThrow();

        ((PointOfInterestTypeAccessor) (Object) leatherWorkerPOI.value()).scorchful$setBlockStates(
                ImmutableSet.<BlockState>builder()
                        .addAll(leatherWorkerPOI.value().matchingStates())
                        .addAll(SPointsOfInterest.SAND_CAULDRONS)
                        .build()
        );
        registerStates(leatherWorkerPOI, SPointsOfInterest.SAND_CAULDRONS);
    }

    public static void registerStates(Holder<PoiType> poiTypeEntry, Set<BlockState> states) {
        states.forEach(state -> {
            Holder<PoiType> existing = PointOfInterestTypesAccessor.scorchful$getStatesToType()
                    .put(state, poiTypeEntry);
            if (existing != null) {
                throw Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", state)));
            }
        });
    }

    private SPointsOfInterest() {

    }

}
