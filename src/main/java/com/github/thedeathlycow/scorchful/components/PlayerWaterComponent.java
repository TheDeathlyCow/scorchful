package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        if (!this.provider.thermoo$isCold()) {
            this.tickWater(this.provider);
        }
    }

    private void tickWater(PlayerEntity player) {
        ScorchfulConfig config = Scorchful.getConfig();

        // sweating: move body water to wetness
        if (ScorchfulIntegrations.isDehydrationLoaded()) {
            this.tickSweatDehydration(config.integrationConfig.dehydrationConfig, player);
        } else {
            this.tickSweatNormal(player);
        }
    }

    private void tickSweatNormal(PlayerEntity player) {
        if (this.waterDrunk > 0 && player.thermoo$getTemperature() > 0) {
            this.waterDrunk--;
            player.thermoo$addWetTicks(2);
        }
    }

    private void tickSweatDehydration(DehydrationConfig config, PlayerEntity player) {
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();
        if (thirstManager.getThirstLevel() > config.getMinWaterLevelForSweat()
                && player.thermoo$getTemperature() > 0) {
            thirstManager.addDehydration(config.getDehydrationConsumedBySweat());
            player.thermoo$addWetTicks(2);
        }
    }
}
