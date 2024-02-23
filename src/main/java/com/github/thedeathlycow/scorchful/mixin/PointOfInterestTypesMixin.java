package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.registry.SPointsOfInterest;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.HashSet;
import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public class PointOfInterestTypesMixin {

    @Shadow @Final private static Set<BlockState> CAULDRONS;

    @ModifyArg(
            method = "registerAndGetDefault",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/util/Set;II)Lnet/minecraft/world/poi/PointOfInterestType;",
                    ordinal = 7
            ),
            index = 2
    )
    private static Set<BlockState> registerSandCauldronsToLeatherworker(Set<BlockState> states) {

        if (states.containsAll(CAULDRONS)) {
            return ImmutableSet.<BlockState>builder()
                    .addAll(states)
                    .addAll(SPointsOfInterest.SAND_CAULDRONS)
                    .build();
        }

        return states;
    }


}
