package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;

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

    // REHYDRATION EXPLANATION
    // The Rehydration enchantment builds up a drink whenever the player loses wetness/soaking to cooling
    // That drink is the same size as a hydrating drink.
    // When the drink is full, the player is given back all the water in the drink as *body* water.
    // However, some of that water is lost, based on the total level of Rehydration.

    public void tickRehydration(double rehydrationEfficiency, int wetChange) {
        if (rehydrationEfficiency > 0) {
            if (wetChange < 0 && this.provider.getRandom().nextBoolean()) {
                int rehydrationCapacity = ServerThirstPlugin.getActivePlugin().getMaxRehydrationWaterCapacity();
                this.waterCaptured = Math.min(this.waterCaptured + 1, rehydrationCapacity);
            }
            this.tickRehydrate(rehydrationEfficiency);
        } else {
            this.resetRehydration();
        }
    }

    private void tickRehydrate(double rehydrationEfficiency) {
        int rehydrationCapacity = ServerThirstPlugin.getActivePlugin().getMaxRehydrationWaterCapacity();
        if (this.waterCaptured >= rehydrationCapacity && this.provider.getWorld() instanceof ServerWorld serverWorld) {
            ServerThirstPlugin plugin = ServerThirstPlugin.getActivePlugin();
            plugin.rehydrateFromEnchantment(this.provider, this.waterCaptured, rehydrationEfficiency);
            this.playRehydrationEffects(serverWorld);
            this.resetRehydration();
        }
    }

    private void resetRehydration() {
        this.waterCaptured = 0;
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
}