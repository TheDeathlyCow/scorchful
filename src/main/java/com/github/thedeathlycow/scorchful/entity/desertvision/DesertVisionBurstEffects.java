package com.github.thedeathlycow.scorchful.entity.desertvision;

import com.github.thedeathlycow.scorchful.entity.DesertVisionEntity;
import com.github.thedeathlycow.scorchful.event.DesertVisionActivation;
import net.minecraft.entity.player.PlayerEntity;

public class DesertVisionBurstEffects {

    public static void initialize() {
        DesertVisionActivation.EVENT.register(DesertVisionBurstEffects::applyEffects);
    }

    private static void applyEffects(DesertVisionEntity vision, PlayerEntity player) {

    }

}
