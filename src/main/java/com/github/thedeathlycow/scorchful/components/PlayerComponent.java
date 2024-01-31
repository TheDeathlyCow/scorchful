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

    private static final String REHYDRATION_DRINK_KEY = "rehydration_drink";


    private final PlayerEntity provider;

    private int waterDrunk = 0;

    private int rehydrationDrink = 0;

    public PlayerComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public int getWaterDrunk() {
        return waterDrunk;
    }

    public void drink(int amount) {
        this.waterDrunk = MathHelper.clamp(this.waterDrunk + amount, 0, MAX_WATER);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(WATER_KEY, NbtElement.INT_TYPE)) {
            this.waterDrunk = tag.getInt(WATER_KEY);
        }

        if (tag.contains(REHYDRATION_DRINK_KEY, NbtElement.INT_TYPE)) {
            this.rehydrationDrink = tag.getInt(REHYDRATION_DRINK_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.waterDrunk > 0) {
            tag.putInt(WATER_KEY, this.waterDrunk);
        }

        if (this.rehydrationDrink > 0) {
            tag.putInt(REHYDRATION_DRINK_KEY, this.rehydrationDrink);
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
        if (this.waterDrunk > 0 && this.provider.thermoo$getTemperature() > 0) {
            this.waterDrunk--;
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
        }
    }

    public void tickRehydration(ThirstConfig config) {
        int rehydrationLevel = SEnchantmentHelper.getTotalRehydrationForPlayer(this.provider);

        if (rehydrationLevel == 0) {
            this.rehydrationDrink = 0;
            return;
        }

        // REHYDRATION EXPLANATION
        // The Rehydration enchantment builds up a drink whenever the player loses wetness (not body water) to cooling
        // That drink is the same size as a hydrating drink.
        // When the drink is full, the player is given back all the water in the drink as *body* water.
        // However, some of that water is lost, based on the total level of Rehydration.

        int rehydrationCapacity = config.getWaterFromHydratingFood();
        this.rehydrationDrink = Math.min(this.rehydrationDrink + 1, rehydrationCapacity);

        if (rehydrationDrink == rehydrationCapacity && this.waterDrunk <= 1) {
            this.rehydrate(config, rehydrationLevel);
        }
    }

    private void rehydrate(ThirstConfig config, int rehydrationLevel) {

        float efficiency = getRehydrationEfficiency(
                rehydrationLevel,
                0f, config.getMaxRehydrationEfficiency()
        );
        int drinkToAdd = MathHelper.floor(this.rehydrationDrink * efficiency);

        this.rehydrationDrink = 0;

        if (drinkToAdd > 0 && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            this.drink(drinkToAdd);
            this.playRehydrationEffects(serverWorld);
        }
    }

    private void playRehydrationEffects(ServerWorld serverWorld) {
        Vec3d pos = this.provider.getPos();

        if (!this.provider.isSilent() && !this.provider.isSneaking()) {
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
