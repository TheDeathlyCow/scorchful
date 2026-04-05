package com.github.thedeathlycow.scorchful.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

/// the purpose of this mixin is to allow [BreezeMixin] to override spawn rules and allow breezes to spawn in daylight
@Mixin(PathfinderMob.class)
public abstract class PathfinderMobMixin {
    @WrapMethod(method = "checkSpawnRules")
    protected boolean overrideCheckSpawnRules(LevelAccessor level, EntitySpawnReason spawnReason, Operation<Boolean> original) {
        return original.call(level, spawnReason);
    }
}