package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

public class PlayerComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 1000;

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
        this.tickWater();
    }

    private void tickWater() {
        if (this.provider.thermoo$isCold()) {
            return;
        }

        HeatingConfig config = Scorchful.getConfig().heatingConfig;

        if (this.water > 0) {
            this.water--;
            this.provider.thermoo$setWetTicks(
                    this.provider.thermoo$getWetTicks() + config.getWetnessFromBodyWater()
            );
        }

        int wetTicks = this.provider.thermoo$getWetTicks();
        if (wetTicks > 0) {
            this.provider.thermoo$setWetTicks(wetTicks - 1);
            this.provider.thermoo$addTemperature(config.getTemperatureFromWetness());
        }
    }

}
