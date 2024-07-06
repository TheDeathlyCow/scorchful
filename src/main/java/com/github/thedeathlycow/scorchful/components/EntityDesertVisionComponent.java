package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.event.DesertVisionActivation;
import com.github.thedeathlycow.scorchful.registry.SDesertVisionControllers;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.temperature.desertvision.DesertVisionController;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntityDesertVisionComponent implements Component, AutoSyncedComponent, ServerTickingComponent {

    private static final double ACTIVATION_DISTANCE = 8.0;

    private final Entity provider;

    @Nullable
    private PlayerEntity cause;

    @Nullable
    private DesertVisionController vision;

    private int timeToLive = 10 * 20;

    public EntityDesertVisionComponent(Entity provider) {
        this.provider = provider;
    }

    public boolean hasDesertVision() {
        return this.cause != null;
    }

    public void applyDesertVision(@NotNull DesertVisionController vision, @NotNull PlayerEntity cause) {
        this.cause = cause;
        this.vision = vision;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        // visions should be transient, so not saved to NBT
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // visions should be transient, so not saved to NBT
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeOptional(Optional.ofNullable(cause), (pBuf, player) -> pBuf.writeUuid(player.getUuid()));
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        UUID uuid = buf.readOptional(PacketByteBuf::readUuid).orElse(null);

        this.cause = uuid != null
                ? this.provider.getWorld().getPlayerByUuid(uuid)
                : null;
    }

    @Override
    public void serverTick() {
        if (!this.tickCanLive()) {
            this.provider.discard();
        }
    }

    private boolean tickCanLive() {
        if (this.timeToLive-- <= 0) {
            return false;
        } else if (this.cause != null) {
            if (this.provider.squaredDistanceTo(cause) < ACTIVATION_DISTANCE * ACTIVATION_DISTANCE) {
                DesertVisionActivation.EVENT.invoker().onActivated(vision, cause);
                return false;
            }
            return cause.hasStatusEffect(SStatusEffects.HEAT_STROKE);
        }
        return true;
    }
}
