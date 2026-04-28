package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PlayerWaterComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 300;

    private static final String WATER_KEY = "body_water";

    private static final String REHYDRATION_DRINK_KEY = "rehydration_drink";


    private final Player provider;

    private int waterDrunk = 0;

    public PlayerWaterComponent(Player provider) {
        this.provider = provider;
    }

    public int getWaterDrunk() {
        return waterDrunk;
    }

    public void drink(int amount) {
        this.waterDrunk = Mth.clamp(this.waterDrunk + amount, 0, MAX_WATER);
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (tag.contains(WATER_KEY, Tag.TAG_INT)) {
            this.waterDrunk = tag.getInt(WATER_KEY);
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
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
