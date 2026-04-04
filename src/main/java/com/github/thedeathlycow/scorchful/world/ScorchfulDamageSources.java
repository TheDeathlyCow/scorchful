package com.github.thedeathlycow.scorchful.world;

import net.minecraft.world.damagesource.DamageSource;
import org.apache.commons.lang3.NotImplementedException;

public interface ScorchfulDamageSources {
    default DamageSource scorchfulSuffocate() {
        throw new NotImplementedException("Implemented in mixin");
    }
}