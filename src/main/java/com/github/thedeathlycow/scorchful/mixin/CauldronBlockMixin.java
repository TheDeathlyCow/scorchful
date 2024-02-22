package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {

    @Inject(
            method = "precipitationTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void allowFillDuringSandstorm(
            BlockState state,
            World world,
            BlockPos pos,
            Biome.Precipitation precipitation,
            CallbackInfo ci
    ) {
        if (SandAccumulation.cauldronSandstormTick(state, world, pos)) {
            ci.cancel();
        }
    }

}
