package com.github.thedeathlycow.scorchful.mixin.server;

import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class SandAccumulationServerWorldMixin extends World {

    protected SandAccumulationServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(
            method = "tickChunk",
            at = @At("TAIL")
    )
    private void doSandPileAccumulation(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        Profiler profiler = this.getProfiler();
        profiler.push("scorchful_sandpiles");
        SandAccumulation.tickChunk((ServerWorld) (Object) this, chunk, randomTickSpeed);
        profiler.pop();
    }
}
