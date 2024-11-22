package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class RehydrationComponent implements Component {

    private final PlayerEntity provider;
    private int waterCaptured = 0;

    private static final String REHYDRATION_DRINK_KEY = "rehydration_drink";

    public RehydrationComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains(REHYDRATION_DRINK_KEY, NbtElement.INT_TYPE)) {
            this.waterCaptured = tag.getInt(REHYDRATION_DRINK_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.waterCaptured > 0) {
            tag.putInt(REHYDRATION_DRINK_KEY, this.waterCaptured);
        }
    }

    public void tickRehydration(double rehydrationEfficiency, int wetChange) {
        if (rehydrationEfficiency > 0) {
            ScorchfulConfig config = Scorchful.getConfig();

            boolean dehydrationLoaded = ScorchfulIntegrations.isDehydrationLoaded();
            if (wetChange < 0 && this.provider.getRandom().nextBoolean()) {
                this.tickRehydrationWaterRecapture(config, dehydrationLoaded);
            }
            this.tickRehydrationRefill(config, rehydrationEfficiency, dehydrationLoaded);
        } else {
            this.resetRehydration();
        }
    }

    // REHYDRATION EXPLANATION
    // The Rehydration enchantment builds up a drink whenever the player loses wetness (not body water) to cooling
    // That drink is the same size as a hydrating drink.
    // When the drink is full, the player is given back all the water in the drink as *body* water.
    // However, some of that water is lost, based on the total level of Rehydration.

    public void tickRehydrationWaterRecapture(ScorchfulConfig config, boolean dehydrationLoaded) {
        int rehydrationCapacity = config.getRehydrationDrinkSize(dehydrationLoaded);
        this.waterCaptured = Math.min(this.waterCaptured + 1, rehydrationCapacity);
    }

    public void tickRehydrationRefill(ScorchfulConfig config, double rehydrationEfficiency, boolean dehydrationLoaded) {
        int rehydrationCapacity = config.getRehydrationDrinkSize(dehydrationLoaded);
        if (waterCaptured >= rehydrationCapacity) {
            if (dehydrationLoaded) {
                this.rehydrateWithDehydration(config, rehydrationEfficiency);
            } else {
                this.rehydrate(config, rehydrationEfficiency);
            }
        }
    }

    public void resetRehydration() {
        this.waterCaptured = 0;
    }

    private void rehydrate(ScorchfulConfig config, double rehydrationEfficiency) {
        // dont drink if dont have to - prevents rehydration spam
        PlayerWaterComponent waterComponent = ScorchfulComponents.PLAYER_WATER.get(this.provider);
        if (waterComponent.getWaterDrunk() > 1) {
            return;
        }

        double efficiency = config.thirstConfig.getMaxRehydrationEfficiency() * rehydrationEfficiency;
        int drinkToAdd = MathHelper.floor(this.waterCaptured * efficiency);

        if (drinkToAdd > 0 && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            waterComponent.drink(drinkToAdd);
            this.playRehydrationEffects(serverWorld);
            this.resetRehydration();
        }
    }

    private void rehydrateWithDehydration(ScorchfulConfig config, double rehydrationEfficiency) {
        ThirstManager thirstManager = ((ThirstManagerAccess) this.provider).getThirstManager();

        DehydrationConfig dehydrationConfig = config.integrationConfig.dehydrationConfig;
        // dont drink if dont have to - prevents rehydration spam
        if (thirstManager.getThirstLevel() > dehydrationConfig.getMinWaterLevelForSweat()) {
            return;
        }

        if (this.provider.getWorld() instanceof ServerWorld serverWorld) {
            int maxWater = MathHelper.floor(
                    rehydrationEfficiency * dehydrationConfig.getMaxWaterLost()
            );
            int waterToAdd = this.provider.getRandom().nextBetween(1, maxWater);
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
}