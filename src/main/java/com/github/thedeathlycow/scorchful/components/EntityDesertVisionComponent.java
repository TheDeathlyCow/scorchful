package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.DesertVisionActivation;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntityDesertVisionComponent implements Component, AutoSyncedComponent, ServerTickingComponent {


    private final Entity provider;

    @Nullable
    private PlayerEntity cause;

    @Nullable
    private HeatVision vision;

    private int timeToLive = 10 * 20;

    public EntityDesertVisionComponent(Entity provider) {
        this.provider = provider;
    }

    public boolean hasDesertVision() {
        return this.cause != null;
    }

    public void applyDesertVision(@NotNull HeatVision vision, @NotNull PlayerEntity cause) {
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

        Scorchful.LOGGER.debug("Writing sync packet to entity desert vision");
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        UUID uuid = buf.readOptional(PacketByteBuf::readUuid).orElse(null);

        this.cause = uuid != null
                ? this.provider.getWorld().getPlayerByUuid(uuid)
                : null;

        Scorchful.LOGGER.debug("Applying sync packet to entity desert vision");
    }

    @Override
    public void serverTick() {
        if (!this.tickCanLive()) {
            Scorchful.LOGGER.debug("Discarding entity desert vision " + this.provider);
            this.provider.discard();
        }
    }

    private boolean tickCanLive() {
        if (this.cause == null) {
            return true;
        } else if (this.timeToLive-- <= 0) {
            return false;
        } else {
            double activationDistance = HeatVision.ACTIVATION_DISTANCE * HeatVision.ACTIVATION_DISTANCE;
            if (this.provider.squaredDistanceTo(cause) < activationDistance) {
                DesertVisionActivation.EVENT.invoker().onActivated(this.vision, cause);
                return false;
            }
            return cause.hasStatusEffect(SStatusEffects.HEAT_STROKE);
        }
    }
}
