package com.github.thedeathlycow.scorchful.block;

import net.minecraft.util.StringIdentifiable;

public enum WaterSaturationLevel implements StringIdentifiable {

    DRY("dry"),
    MOIST("moist"),
    WET("wet"),
    SOAKED("soaked");

    private final String name;

    WaterSaturationLevel(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
