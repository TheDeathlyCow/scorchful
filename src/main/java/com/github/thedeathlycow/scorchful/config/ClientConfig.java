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

    boolean enableSandstormFog = true;

    int sandStormParticleRenderDistance = 20;

    int sandStormParticleRarity = 60;

    float sandStormParticleVelocity = -1f;

    float sandStormFogStart = 16f;

    float sandStormFogEnd = 64f;


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

    public boolean isSandstormFogEnabled() {
        return enableSandstormFog;
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

    public float getSandStormFogStart() {
        return sandStormFogStart;
    }

    public float getSandStormFogEnd() {
        return sandStormFogEnd;
    }
}
