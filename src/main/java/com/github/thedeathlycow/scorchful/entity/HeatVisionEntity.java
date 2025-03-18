package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.EntityState;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class HeatVisionEntity extends Entity {
    private static final String HEAT_VISION_KEY = "heat_vision";

    public HeatVisionEntity(EntityType<?> type, World world, RegistryEntry<HeatVisionDefinition> heatVision) {
        super(type, world);
        ScorchfulComponents.HEAT_VISION_SYNCED_DATA.get(this).heatVision = heatVision;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // handled by cca
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains(HEAT_VISION_KEY)) {
            RegistryOps<NbtElement> ops = this.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE);
            ScorchfulComponents.HEAT_VISION_SYNCED_DATA.get(this).heatVision = HeatVisionDefinition.CODEC.decode(ops, nbt.get(HEAT_VISION_KEY))
                    .getOrThrow()
                    .getFirst();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // handled by cca
    }

    public RegistryEntry<HeatVisionDefinition> getVisionData() {
        return ScorchfulComponents.HEAT_VISION_SYNCED_DATA.get(this).heatVision;
    }

    public HeatVisionType getRenderType() {
        return this.getVisionData().value().renderType();
    }

    public EntityState getEntityRenderState() {
        return this.getVisionData().value().entityState().orElse(null);
    }

    public static class SyncedData implements Component, AutoSyncedComponent {
        private final HeatVisionEntity provider;

        private RegistryEntry<HeatVisionDefinition> heatVision;

        public SyncedData(HeatVisionEntity provider) {
            this.provider = provider;
        }

        @Override
        public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            RegistryOps<NbtElement> ops = provider.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE);
            this.heatVision = HeatVisionDefinition.CODEC.decode(ops, nbt.get(HEAT_VISION_KEY))
                    .getOrThrow()
                    .getFirst();
        }

        @Override
        public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            RegistryOps<NbtElement> ops = provider.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE);
            nbt.put(
                    HEAT_VISION_KEY,
                    HeatVisionDefinition.CODEC.encodeStart(ops, this.heatVision)
                            .getOrThrow()
            );
        }

        @Override
        public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
            HeatVisionDefinition.PACKET_CODEC.encode(buf, this.heatVision);
        }

        @Override
        public void applySyncPacket(RegistryByteBuf buf) {
            this.heatVision = HeatVisionDefinition.PACKET_CODEC.decode(buf);
        }
    }
}