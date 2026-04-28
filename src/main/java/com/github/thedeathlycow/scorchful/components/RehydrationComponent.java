package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;

public class RehydrationComponent implements Component {
    private final Player provider;
    private int waterCaptured = 0;
    private static final String WATER_CAPTURED_KEY = "water_captured";

    public RehydrationComponent(Player provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (tag.contains(WATER_CAPTURED_KEY, Tag.TAG_INT)) {
            this.waterCaptured = tag.getInt(WATER_CAPTURED_KEY);
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (this.waterCaptured > 0) {
            tag.putInt(WATER_CAPTURED_KEY, this.waterCaptured);
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
                int rehydrationCapacity = ServerThirstPlugin.getActivePlugin().getRehydrationThreshold();
                this.waterCaptured = Math.min(this.waterCaptured + 1, rehydrationCapacity);
            }
            this.tickRehydrate(rehydrationEfficiency);
        } else {
            this.resetRehydration();
        }
    }

    private void tickRehydrate(double rehydrationEfficiency) {
        int rehydrationCapacity = ServerThirstPlugin.getActivePlugin().getRehydrationThreshold();
        if (this.waterCaptured >= rehydrationCapacity && this.provider.level() instanceof ServerLevel serverWorld) {
            ServerThirstPlugin plugin = ServerThirstPlugin.getActivePlugin();
            plugin.rehydrateFromEnchantment(this.provider, this.waterCaptured, rehydrationEfficiency);
            this.playRehydrationEffects(serverWorld);
            this.resetRehydration();
        }
    }

    private void resetRehydration() {
        this.waterCaptured = 0;
    }

    private void playRehydrationEffects(ServerLevel serverWorld) {
        Vec3 pos = this.provider.position();

        if (!this.provider.isSilent() && !this.provider.isShiftKeyDown()) {
            serverWorld.playSound(
                    null,
                    this.provider.blockPosition(),
                    SSoundEvents.REHYDRATE,
                    this.provider.getSoundSource()
            );
        }

        if (!this.provider.isInvisible()) {
            float height = this.provider.getBbHeight();
            float width = this.provider.getBbWidth();

            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE_POP,
                    pos.x, pos.y, pos.z,
                    50,
                    width, height, width,
                    1f / 1000f
            );
        }
    }
}