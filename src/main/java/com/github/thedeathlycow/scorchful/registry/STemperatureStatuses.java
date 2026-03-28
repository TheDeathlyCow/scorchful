package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.resources.ResourceKey;

public final class STemperatureStatuses {
    public static final ResourceKey<TemperatureStatus> HEAT_DAMAGE = key("heat_damage");

    public static final ResourceKey<TemperatureStatus> DOG_PANTING = key("dog/panting");

    private static ResourceKey<TemperatureStatus> key(String name) {
        return ResourceKey.create(ThermooRegistries.TEMPERATURE_STATUS, Scorchful.id(name));
    }

    private STemperatureStatuses() {

    }
}