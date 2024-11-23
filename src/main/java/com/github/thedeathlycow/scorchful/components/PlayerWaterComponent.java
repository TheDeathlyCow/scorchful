package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

public class PlayerWaterComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 300;

    private static final String WATER_KEY = "body_water";

    private static final String REHYDRATION_DRINK_KEY = "rehydration_drink";


    private final PlayerEntity provider;

    private int waterDrunk = 0;

    public PlayerWaterComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public int getWaterDrunk() {
        return waterDrunk;
    }

    public void drink(int amount) {
        this.waterDrunk = MathHelper.clamp(this.waterDrunk + amount, 0, MAX_WATER);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains(WATER_KEY, NbtElement.INT_TYPE)) {
            this.waterDrunk = tag.getInt(WATER_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.waterDrunk > 0) {
            tag.putInt(WATER_KEY, this.waterDrunk);
        }
    }

    @Override
    public void serverTick() {
        if (!this.provider.thermoo$isCold() && ServerThirstPlugin.getActivePlugin().dehydrateFromSweating(this.provider)) {
            // sweating: move thirst water to wetness
            this.provider.thermoo$addWetTicks(2);
        }
    }
}
