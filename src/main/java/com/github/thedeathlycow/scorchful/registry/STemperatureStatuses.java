package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.resources.ResourceKey;

public final class STemperatureStatuses {
    public static final ResourceKey<TemperatureStatus> HEAT_DAMAGE = key("heat_damage");

    public static final ResourceKey<TemperatureStatus> DOG_PANTING = key("dog/panting");

    public static final ResourceKey<TemperatureStatus> PLAYER_WARM = key("player/warm");
    public static final ResourceKey<TemperatureStatus> PLAYER_HOT = key("player/hot");
    public static final ResourceKey<TemperatureStatus> PLAYER_OVERHEATING = key("player/overheating");
    public static final ResourceKey<TemperatureStatus> PLAYER_HEAT_STROKE = key("player/heat_stroke");

    public static final ResourceKey<TemperatureStatus> PLAYER_HEART_BEAT_SLOW = key("player/heart_beat/slow");
    public static final ResourceKey<TemperatureStatus> PLAYER_HEART_BEAT_MEDIUM = key("player/heart_beat/medium");
    public static final ResourceKey<TemperatureStatus> PLAYER_HEART_BEAT_FAST = key("player/heart_beat/fast");
    public static final ResourceKey<TemperatureStatus> PLAYER_HEART_BEAT_RACING = key("player/heart_beat/racing");

    public static final ResourceKey<TemperatureStatus> STRIDER_HOT = key("strider/hot");
    public static final ResourceKey<TemperatureStatus> STRIDER_SPEED = key("strider/speed");

    private static ResourceKey<TemperatureStatus> key(String name) {
        return ResourceKey.create(ThermooRegistries.TEMPERATURE_STATUS, Scorchful.id(name));
    }

    private STemperatureStatuses() {

    }
}