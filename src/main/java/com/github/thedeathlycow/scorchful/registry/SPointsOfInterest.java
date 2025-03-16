package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.mixin.accessor.PointOfInterestTypeAccessor;
import com.github.thedeathlycow.scorchful.mixin.accessor.PointOfInterestTypesAccessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

public final class SPointsOfInterest {
    public static final Set<BlockState> SAND_CAULDRONS = Stream.of(
                    SBlocks.SAND_CAULDRON,
                    SBlocks.RED_SAND_CAULDRON
            )
            .flatMap(block -> block.getStateManager().getStates().stream())
            .collect(ImmutableSet.toImmutableSet());

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful POIs");
        RegistryEntry<PointOfInterestType> leatherWorkerPOI = Registries.POINT_OF_INTEREST_TYPE
                .getEntry(PointOfInterestTypes.LEATHERWORKER)
                .orElseThrow();

        ((PointOfInterestTypeAccessor) (Object) leatherWorkerPOI.value()).scorchful$setBlockStates(
                ImmutableSet.<BlockState>builder()
                        .addAll(leatherWorkerPOI.value().blockStates())
                        .addAll(SPointsOfInterest.SAND_CAULDRONS)
                        .build()
        );
        registerStates(leatherWorkerPOI, SPointsOfInterest.SAND_CAULDRONS);
    }

    private static void registerStates(RegistryEntry<PointOfInterestType> poiTypeEntry, Set<BlockState> states) {
        states.forEach(state -> {
            RegistryEntry<PointOfInterestType> existing = PointOfInterestTypesAccessor.scorchful$getStatesToType()
                    .put(state, poiTypeEntry);
            if (existing != null) {
                throw Util.throwOrPause(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", state)));
            }
        });
    }

    private SPointsOfInterest() {

    }

}
