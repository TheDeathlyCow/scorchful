package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PlayerComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 300;

    private static final String WATER_KEY = "body_water";


    private final PlayerEntity provider;

    private int water = 0;

    public PlayerComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public PlayerEntity getProvider() {
        return provider;
    }

    public int getWater() {
        return water;
    }

    public void addWater(int amount) {
        this.water = MathHelper.clamp(this.water + amount, 0, MAX_WATER);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(WATER_KEY, NbtElement.INT_TYPE)) {
            this.water = tag.getInt(WATER_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.water > 0) {
            tag.putInt(WATER_KEY, this.water);
        }
    }

    @Override
    public void serverTick() {
        if (!this.provider.thermoo$isCold()) {
            this.tickWater();
        }
    }

    private void tickWater() {
        ThirstConfig config = Scorchful.getConfig().thirstConfig;

        if (this.water > 0 && this.provider.thermoo$getTemperature() > 0) {
            this.water--;
            this.provider.thermoo$setWetTicks(this.provider.thermoo$getWetTicks() + config.getWetnessFromBodyWater());
        }

        if (this.provider.thermoo$isWet()) {

            int temperatureChange = config.getTemperatureFromWetness();
            World world = this.provider.getWorld();
            if (world.getBiome(this.provider.getBlockPos()).isIn(SBiomeTags.HUMID_BIOMES)) {
                temperatureChange = MathHelper.floor(temperatureChange * config.getHumidBiomeWetEfficieny());
            }

            this.provider.thermoo$addTemperature(temperatureChange);
        }
    }

}
