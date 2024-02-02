package com.github.thedeathlycow.scorchful.block;

import net.minecraft.util.StringIdentifiable;

public enum WaterSaturationLevel implements StringIdentifiable {

    DRY("dry", 0, 0f),
    MOIST("moist", 0, 0f),
    WET("wet", 1, 0f),
    SOAKED("soaked", 8, 0.25f);

    private final String name;

    private final int particlesPerTick;

    private final float splashChance;

    WaterSaturationLevel(String name, int particlesPerTick, float splashChance) {
        this.name = name;
        this.particlesPerTick = particlesPerTick;
        this.splashChance = splashChance;
    }

    public float getSplashChance() {
        return splashChance;
    }

    public int getParticlesPerDisplayTick() {
        return particlesPerTick;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
