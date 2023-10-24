package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class PlayerComponent implements Component, ServerTickingComponent {

    private static final String WATER_KEY = "body_water";


    private final PlayerEntity provider;

    public int water = 0;

    public PlayerComponent(PlayerEntity provider) {
        this.provider = provider;
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
        if (this.provider.thermoo$getTemperatureScale() > 0.5f) {
            // tick water
        }
    }
}
