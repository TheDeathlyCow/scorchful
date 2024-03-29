package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerComponent implements Component, ServerTickingComponent {

    public static final int MAX_WATER = 300;

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
            this.tickWater(this.provider);
        }
    }

    private void tickWater(PlayerEntity player) {
        ScorchfulConfig config = Scorchful.getConfig();

        // sweating: move body water to wetness
        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.DEHYDRATION_ID)) {
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

    // REHYDRATION EXPLANATION
    // The Rehydration enchantment builds up a drink whenever the player loses wetness (not body water) to cooling
    // That drink is the same size as a hydrating drink.
    // When the drink is full, the player is given back all the water in the drink as *body* water.
    // However, some of that water is lost, based on the total level of Rehydration.

    public void tickRehydrationWaterRecapture(ScorchfulConfig config, boolean dehydrationLoaded) {
        int rehydrationCapacity = getRehydrationDrinkSize(config, dehydrationLoaded);
        this.rehydrationDrink = Math.min(this.rehydrationDrink + 1, rehydrationCapacity);
    }

    public void tickRehydration(ScorchfulConfig config, int rehydrationLevel, boolean dehydrationLoaded) {
        int rehydrationCapacity = getRehydrationDrinkSize(config, dehydrationLoaded);
        if (rehydrationDrink >= rehydrationCapacity) {
            if (dehydrationLoaded) {
                this.rehydrateWithDehydration(config, rehydrationLevel);
            } else {
                this.rehydrate(config, rehydrationLevel);
            }
        }
    }

    public void resetRehydration() {
        this.rehydrationDrink = 0;
    }

    private void rehydrate(ScorchfulConfig config, int rehydrationLevel) {
        // dont drink if dont have to - prevents rehydration spam
        if (this.waterDrunk > 1) {
            return;
        }

        float efficiency = getRehydrationEfficiency(
                rehydrationLevel,
                0f, config.thirstConfig.getMaxRehydrationEfficiency()
        );
        int drinkToAdd = MathHelper.floor(this.rehydrationDrink * efficiency);

        if (drinkToAdd > 0 && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            this.drink(drinkToAdd);
            this.playRehydrationEffects(serverWorld);
            this.resetRehydration();
        }
    }

    private void rehydrateWithDehydration(ScorchfulConfig config, int rehydrationLevel) {
        ThirstManager thirstManager = ((ThirstManagerAccess) this.provider).getThirstManager();

        DehydrationConfig dehydrationConfig = config.integrationConfig.dehydrationConfig;
        // dont drink if dont have to - prevents rehydration spam
        if (thirstManager.getThirstLevel() > dehydrationConfig.getMinWaterLevelForSweat()) {
            return;
        }

        int waterToAdd = rehydrationLevel * dehydrationConfig.getRehydrationWaterAddedPerLevel();

        if (waterToAdd > 0 && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            thirstManager.add(waterToAdd);
            this.playRehydrationEffects(serverWorld);
            this.resetRehydration();
        }
    }

    private void playRehydrationEffects(ServerWorld serverWorld) {
        Vec3d pos = this.provider.getPos();

        if (!this.provider.isSilent() && !this.provider.isSneaking()) {
            serverWorld.playSound(
                    null,
                    this.provider.getBlockPos(),
                    SSoundEvents.REHYDRATE,
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

    private static int getRehydrationDrinkSize(ScorchfulConfig config, boolean dehydrationLoaded) {
        return dehydrationLoaded
                ? config.thirstConfig.getRehydrationDrinkSize()
                : config.integrationConfig.dehydrationConfig.getRehydrationDrinkSize();
    }
}
