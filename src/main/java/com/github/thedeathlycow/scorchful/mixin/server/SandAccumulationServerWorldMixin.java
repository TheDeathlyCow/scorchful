package com.github.thedeathlycow.scorchful.mixin.server;

import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;

@Mixin(ServerLevel.class)
public abstract class SandAccumulationServerWorldMixin extends Level {

    protected SandAccumulationServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(
            method = "tickChunk",
            at = @At("TAIL")
    )
    private void doSandPileAccumulation(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ProfilerFiller profiler = this.getProfiler();
        profiler.push("scorchful_sandpiles");
        SandAccumulation.tickChunk((ServerLevel) (Object) this, chunk, randomTickSpeed);
        profiler.pop();
    }
}
