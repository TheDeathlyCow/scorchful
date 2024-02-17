package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".client_config")
public class ClientConfig implements ConfigData {

    boolean doBurningHeartOverlay = true;

    boolean doSunHatShading = true;

    float sunHatShadeOpacity = 0.2f;

    boolean enableSandstormParticles = true;

    int sandStormParticleRenderDistance = 10;

    int sandStormParticleRarity = 20;

    float sandStormParticleVelocity = -0.25f;

    float sandStormParticleScale = 10f;

    public boolean isSunHatShading() {
        return doSunHatShading;
    }

    public float getSunHatShadeOpacity() {
        return sunHatShadeOpacity;
    }

    public boolean doBurningHeartOverlay() {
        return doBurningHeartOverlay;
    }

    public boolean isSandstormParticlesEnabled() {
        return enableSandstormParticles;
    }

    public int getSandStormParticleRenderDistance() {
        return sandStormParticleRenderDistance;
    }

    public int getSandStormParticleRarity() {
        return Math.max(1, sandStormParticleRarity);
    }

    public float getSandStormParticleVelocity() {
        return sandStormParticleVelocity;
    }

    public float getSandStormParticleScale() {
        return sandStormParticleScale;
    }
}
