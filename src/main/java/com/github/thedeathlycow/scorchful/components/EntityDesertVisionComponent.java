package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.HeatVisionActivation;
import com.github.thedeathlycow.scorchful.registry.SMobEffects;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDesertVisionComponent implements Component, AutoSyncedComponent, ServerTickingComponent {


    private final Entity provider;

    @Nullable
    private Player cause;

    @Nullable
    private HeatVision vision;

    private int timeToLive = 30 * 20;

    public EntityDesertVisionComponent(Entity provider) {
        this.provider = provider;
    }

    public boolean hasDesertVision() {
        return this.cause != null;
    }

    public void applyDesertVision(@NotNull HeatVision vision, @NotNull Player cause) {
        this.cause = cause;
        this.vision = vision;
    }

    @Override
    public void readData(ValueInput readView) {
        // visions should be transient, so not saved to NBT
    }

    @Override
    public void writeData(ValueOutput writeView) {
        // visions should be transient, so not saved to NBT
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeOptional(Optional.ofNullable(cause), (pBuf, player) -> pBuf.writeUUID(player.getUUID()));

        Scorchful.LOGGER.debug("Writing sync packet to entity desert vision");
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        UUID uuid = buf.readOptional(RegistryFriendlyByteBuf::readUUID).orElse(null);

        this.cause = uuid != null
                ? this.provider.level().getPlayerByUUID(uuid)
                : null;

        Scorchful.LOGGER.debug("Applying sync packet to entity desert vision");
    }

    @Override
    public void serverTick() {
        if (!this.tickCanLive()) {
            Scorchful.LOGGER.debug("Discarding entity desert vision {}", this.provider);
            this.provider.discard();
        }
    }

    public Player getCause() {
        return cause;
    }

    private boolean tickCanLive() {
        if (this.cause == null) {
            return true;
        } else if (this.timeToLive-- <= 0) {
            return false;
        } else {
            double activationDistance = HeatVision.ACTIVATION_DISTANCE * HeatVision.ACTIVATION_DISTANCE;
            if (this.provider.distanceToSqr(cause) < activationDistance) {
                HeatVisionActivation.EVENT.invoker().onActivated(
                        this.vision,
                        (ServerLevel) this.provider.level(),
                        this.provider.blockPosition(),
                        cause
                );
                return false;
            }
            return cause.hasEffect(SMobEffects.HEAT_STROKE);
        }
    }
}
