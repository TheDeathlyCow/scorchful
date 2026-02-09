package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public void readData(ValueInput readView) {
        this.waterDrunk = readView.getIntOr(WATER_KEY, 0);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        if (this.waterDrunk > 0) {
            writeView.putInt(WATER_KEY, this.waterDrunk);
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
