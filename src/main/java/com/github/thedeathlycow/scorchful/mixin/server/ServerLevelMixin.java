package com.github.thedeathlycow.scorchful.mixin.server;

import com.github.thedeathlycow.scorchful.world.SandAccumulation;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(
            method = "tickChunk",
            at = @At("TAIL")
    )
    private void doSandPileAccumulation(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("scorchful_sandpiles");
        SandAccumulation.tickChunk((ServerLevel) (Object) this, chunk, randomTickSpeed);
        profiler.pop();
    }
}
