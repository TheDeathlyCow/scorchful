package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.entity.effect.MesmerizedStatusEffect;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.List;
import java.util.Optional;

public class MesmerizedComponent implements Component, ServerTickingComponent, AutoSyncedComponent {

    private static final double MOVEMENT_SPEED = 0.07;

    private final LivingEntity provider;

    private final ComponentKey<MesmerizedComponent> key;

    private LivingEntity mesmerizedTarget;

    public MesmerizedComponent(LivingEntity provider, ComponentKey<MesmerizedComponent> key) {
        this.provider = provider;
        this.key = key;
    }

    @Nullable
    public LivingEntity getMesmerizedTarget() {
        return mesmerizedTarget;
    }

    public Optional<LivingEntity> maybeGetMesmerizedTarget() {
        return Optional.ofNullable(this.getMesmerizedTarget());
    }

    public void setMesmerizedTarget(LivingEntity mesmerizedTarget) {
        this.mesmerizedTarget = mesmerizedTarget;
        this.key.sync(this.provider);
    }

    public boolean isMesmerized() {
        return hasMesmerizedTarget() && this.provider.hasStatusEffect(SStatusEffects.MESMERIZED);
    }

    public boolean hasMesmerizedTarget() {
        return this.mesmerizedTarget != null;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return this.provider == player;
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeOptional(this.maybeGetMesmerizedTarget(), (b, value) -> {
            b.writeVarInt(value.getId());
        });
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.mesmerizedTarget = buf.readOptional(PacketByteBuf::readVarInt)
                .map(value -> {
                    Entity entity = this.provider.getWorld().getEntityById(value);
                    return entity instanceof LivingEntity livingEntity ? livingEntity : null;
                }).orElse(null);
    }

    @Override
    public void serverTick() {
        if (!this.provider.hasStatusEffect(SStatusEffects.MESMERIZED)) {
            if (this.hasMesmerizedTarget()) {
                this.setMesmerizedTarget(null);
            }
            return;
        }

        if (this.updateTarget()) {
            this.moveTowardsTarget();
        }
    }

    /**
     * @return Returns true if the provider has a mesmerized target after this call
     */
    private boolean updateTarget() {
        if (this.hasMesmerizedTarget()) {
            return this.checkKeepMesmerizedTarget();
        } else {
            return this.tryAcquireNewMesmerizedTarget();
        }
    }

    private void moveTowardsTarget() {
        if (this.canForceProviderMovement()) {
            Vec3d targetPos = this.mesmerizedTarget.getPos();
            Vec3d providerPos = this.provider.getPos();

            Vec3d moveDirection = targetPos.subtract(providerPos)
                    .normalize()
                    .multiply(MOVEMENT_SPEED);

            this.provider.addVelocity(moveDirection);
            this.provider.velocityModified = true;

            this.provider.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, targetPos);
        }
    }

    /**
     * @return Returns {@code true} if the current target can be kept
     */
    private boolean checkKeepMesmerizedTarget() {
        if (MesmerizedStatusEffect.IS_VALID_TARGET.test(this.mesmerizedTarget)) {
            return true;
        } else {
            this.setMesmerizedTarget(null);
            return false;
        }
    }

    /**
     * @return Returns {@code true} if a new target is found
     */
    private boolean tryAcquireNewMesmerizedTarget() {
        List<LivingEntity> nearby = this.provider.getWorld().getEntitiesByClass(
                LivingEntity.class,
                this.provider.getBoundingBox().expand(16),
                MesmerizedStatusEffect.IS_VALID_TARGET
                        .and(entity -> entity != this.provider)
        );
        if (!nearby.isEmpty()) {
            this.setMesmerizedTarget(nearby.getFirst());
            return true;
        }
        return false;
    }

    private boolean canForceProviderMovement() {
        return this.provider.isPlayer() && !this.provider.isSpectator();
    }
}
