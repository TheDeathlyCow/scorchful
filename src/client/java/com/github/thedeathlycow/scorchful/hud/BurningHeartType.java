package com.github.thedeathlycow.scorchful.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public enum BurningHeartType {


    ENGULFED(10),
    ENGULFED_HARDCORE(20);

    final int textureV;

    BurningHeartType(int textureV) {
        this.textureV = textureV;
    }

    public static BurningHeartType forPlayer(PlayerEntity player) {
        int maxTemperature = player.thermoo$getMaxTemperature();
        int temperature = player.thermoo$getTemperature();
        if (temperature >= maxTemperature - 1) {
            return player.getWorld().getLevelProperties().isHardcore()
                    ? ENGULFED_HARDCORE
                    : ENGULFED;
        }
        return null;
    }

}
