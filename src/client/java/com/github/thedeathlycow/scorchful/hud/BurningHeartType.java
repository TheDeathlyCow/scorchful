package com.github.thedeathlycow.scorchful.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum BurningHeartType {


    ENGULFED(10),
    ENGULFED_HARDCORE(20);

    final int textureV;

    BurningHeartType(int textureV) {
        this.textureV = textureV;
    }

    @Nullable
    public static BurningHeartType forPlayer(@Nullable PlayerEntity player, boolean hardcore) {
        if (player != null) {
            int maxTemperature = player.thermoo$getMaxTemperature();
            int temperature = player.thermoo$getTemperature();
            if (temperature >= maxTemperature - 1) {
                return hardcore
                        ? ENGULFED_HARDCORE
                        : ENGULFED;
            }
        }
        return null;
    }

}
