package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

/// allows breezes to spawn in daylight, if the config option is enabled
@Mixin(Breeze.class)
public class BreezeMixin extends PathfinderMobMixin {
    @Override
    protected boolean overrideCheckSpawnRules(LevelAccessor level, EntitySpawnReason spawnReason, Operation<Boolean> original) {
        return super.overrideCheckSpawnRules(level, spawnReason, original) || ScorchfulConfig.getWeatherConfig().enableBreezesInSandstorms();
    }
}