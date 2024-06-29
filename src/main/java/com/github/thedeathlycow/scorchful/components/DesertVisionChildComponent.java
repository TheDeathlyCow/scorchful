package com.github.thedeathlycow.scorchful.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class DesertVisionChildComponent implements Component, AutoSyncedComponent {

    private final Entity provider;

    private boolean isDesertVisionChild = false;

    @Nullable
    private UUID causingPlayerID;

    public DesertVisionChildComponent(Entity provider) {
        this.provider = provider;
    }

    public boolean isDesertVisionChild() {
        return isDesertVisionChild;
    }

    public void makeDesertVisionChild(PlayerEntity causingPlayer) {
        isDesertVisionChild = true;
        this.causingPlayerID = causingPlayer != null ? causingPlayer.getUuid() : null;
    }

    @Nullable
    public UUID getCausingPlayerID() {
        return causingPlayerID;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        // children should be transient, so not saved to NBT
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // children should be transient, so not saved to NBT
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return AutoSyncedComponent.super.shouldSyncWith(player);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(isDesertVisionChild);

        UUID id = recipient.getUuid().equals(causingPlayerID) ? causingPlayerID : null;
        buf.writeOptional(Optional.ofNullable(id), PacketByteBuf::writeUuid);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.isDesertVisionChild = buf.readBoolean();
        this.causingPlayerID = buf.readOptional(PacketByteBuf::readUuid).orElse(null);
    }
}
