package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {

    @Inject(
            method = "handlePrecipitation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void allowFillDuringSandstorm(
            BlockState state,
            Level world,
            BlockPos pos,
            Biome.Precipitation precipitation,
            CallbackInfo ci
    ) {
        if (SandAccumulation.cauldronSandstormTick(state, world, pos)) {
            ci.cancel();
        }
    }

}
