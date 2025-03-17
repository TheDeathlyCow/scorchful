package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionType;
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
    private static final String HEAT_VISION_TYPE_KEY = "heat_vision_type";

    public HeatVisionEntity(EntityType<?> type, World world, RegistryEntry<HeatVisionType> heatVisionType) {
        super(type, world);
        ScorchfulComponents.HEAT_VISION_SYNCED_DATA.get(this).heatVisionType = heatVisionType;
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
        // handled by cca
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // handled by cca
    }

    public RegistryEntry<HeatVisionType> getHeatVisionType() {
        return ScorchfulComponents.HEAT_VISION_SYNCED_DATA.get(this).heatVisionType;
    }

    public static class SyncedData implements Component, AutoSyncedComponent {
        private final HeatVisionEntity provider;

        private RegistryEntry<HeatVisionType> heatVisionType;

        public SyncedData(HeatVisionEntity provider) {
            this.provider = provider;
        }

        @Override
        public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            RegistryOps<NbtElement> ops = provider.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE);
            this.heatVisionType = HeatVisionType.CODEC.decode(ops, nbt.get(HEAT_VISION_TYPE_KEY))
                    .getOrThrow()
                    .getFirst();
        }

        @Override
        public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            RegistryOps<NbtElement> ops = provider.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE);
            nbt.put(
                    HEAT_VISION_TYPE_KEY,
                    HeatVisionType.CODEC.encodeStart(ops, this.heatVisionType)
                            .getOrThrow()
            );
        }

        @Override
        public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
            HeatVisionType.PACKET_CODEC.encode(buf, this.heatVisionType);
        }

        @Override
        public void applySyncPacket(RegistryByteBuf buf) {
            this.heatVisionType = HeatVisionType.PACKET_CODEC.decode(buf);
        }
    }
}