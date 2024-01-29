package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.enchantment.SEnchantmentHelper;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 600;

    private static final String WATER_KEY = "body_water";


    private final PlayerEntity provider;

    private int water = 0;

    private int capturedWater = 0;

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

        // sweating: move body water to wetness
        if (this.water > 0 && this.provider.thermoo$getTemperature() > 0) {
            this.water--;
            this.provider.thermoo$setWetTicks(this.provider.thermoo$getWetTicks() + 1);
        }

        // cooling: move wetness to temperature
        if (this.provider.thermoo$isWet()) {
            int temperatureChange = config.getTemperatureFromWetness();
            World world = this.provider.getWorld();
            if (world.getBiome(this.provider.getBlockPos()).isIn(SBiomeTags.HUMID_BIOMES)) {
                temperatureChange = MathHelper.floor(temperatureChange * config.getHumidBiomeSweatEfficiency());
            }

            this.provider.thermoo$addTemperature(temperatureChange);

            if (temperatureChange < 0) {
                this.tickRehydration(config);
            }
            //TODO: drying should maybe be moved here
        }
    }

    private void tickRehydration(ThirstConfig config) {
        int rehydrationLevel = SEnchantmentHelper.getTotalRehydrationForPlayer(this.provider);

        if (rehydrationLevel == 0) {
            this.capturedWater = 0;
            return;
        }

        // REHYDRATION EXPLANATION
        // The Rehydration enchantment builds up a drink whenever the player loses wetness (not body water) to cooling
        // That drink is the same size as a hydrating drink.
        // When the drink is full, the player is given back all the water in the drink as *body* water.
        // However, some of that water is lost, based on the total level of Rehydration.

        int rehydrationCapacity = config.getWaterFromHydratingFood();
        this.capturedWater = Math.min(this.capturedWater + 1, rehydrationCapacity);

        if (capturedWater == rehydrationCapacity && this.water <= 1) {
            this.rehydrate(config, rehydrationLevel);
        }
    }

    private void rehydrate(ThirstConfig config, int rehydrationLevel) {

        float efficiency = getRehydrationEfficiency(
                rehydrationLevel,
                config.getMinRehydrationEfficiency(), config.getMaxRehydrationEfficiency()
        );
        int addedWater = MathHelper.floor(this.capturedWater * efficiency);

        this.addWater(addedWater);
        this.capturedWater = 0;

        if (addedWater > 0 && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            this.playRehydrationEffects(serverWorld);
        }
    }

    private void playRehydrationEffects(ServerWorld serverWorld) {
        Vec3d pos = this.provider.getPos();

        if (!this.provider.isSilent()) {
            serverWorld.playSound(
                    null,
                    this.provider.getBlockPos(),
                    SoundEvents.ITEM_BOTTLE_FILL,
                    this.provider.getSoundCategory()
            );
        }

        if (!this.provider.isInvisible()) {
            float height = this.provider.getHeight();
            float width = this.provider.getWidth();

            serverWorld.spawnParticles(
                    ParticleTypes.BUBBLE_POP,
                    pos.x, pos.y, pos.z,
                    50,
                    width, height, width,
                    1f / 1000f
            );
        }
    }

    private static float getRehydrationEfficiency(int rehydrationLevel, float min, float max) {
        return MathHelper.lerp(rehydrationLevel / 4f, min, max);
    }
}
