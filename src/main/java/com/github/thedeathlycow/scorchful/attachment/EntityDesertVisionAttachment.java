package com.github.thedeathlycow.scorchful.attachment;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.HeatVisionActivation;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class EntityDesertVisionAttachment {
    private final Entity provider;

    @Nullable
    private Player cause;

    @Nullable
    private HeatVision vision;

    private int timeToLive = 30 * 20;

    public EntityDesertVisionAttachment(IAttachmentHolder holder) {
        this(holder, null);
    }

    private EntityDesertVisionAttachment(IAttachmentHolder holder, Player cause) {
        this.provider = holder instanceof Entity entity ? entity : null;
        this.cause = cause;

        Objects.requireNonNull(this.provider, "Entity desert vision attachment created on non-entity, this will cause a crash later.");
    }

    public boolean hasDesertVision() {
        return this.cause != null;
    }

    public void applyDesertVision(@NotNull HeatVision vision, @NotNull Player cause) {
        this.cause = cause;
        this.vision = vision;
    }

    public void serverTick() {
        if (!this.tickCanLive()) {
            Scorchful.LOGGER.debug("Discarding entity desert vision " + this.provider);
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
            return cause.hasEffect(SStatusEffects.HEAT_STROKE);
        }
    }

    public static final class SyncHandler implements AttachmentSyncHandler<EntityDesertVisionAttachment> {
        @Override
        public void write(RegistryFriendlyByteBuf buf, EntityDesertVisionAttachment attachment, boolean initialSync) {
            buf.writeOptional(Optional.ofNullable(attachment.cause), (pBuf, player) -> pBuf.writeUUID(player.getUUID()));

            Scorchful.LOGGER.debug("Writing sync packet to entity desert vision");
        }

        @Override
        public EntityDesertVisionAttachment read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, EntityDesertVisionAttachment previousValue) {
            Entity provider = holder instanceof Entity entity ? entity : null;
            Objects.requireNonNull(provider, "Entity desert vision attachment received for non-entity");

            UUID uuid = buf.readOptional(RegistryFriendlyByteBuf::readUUID).orElse(null);
            Player cause = uuid != null
                    ? provider.level().getPlayerByUUID(uuid)
                    : null;

            return new EntityDesertVisionAttachment(holder, cause);
        }
    }
}
