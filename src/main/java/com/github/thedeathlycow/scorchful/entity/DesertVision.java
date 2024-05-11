package com.github.thedeathlycow.scorchful.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class DesertVision extends Entity {

    public DesertVision(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void remove(RemovalReason reason) {

        this.getPassengerList().forEach(passenger -> {
            if (passenger instanceof DisplayEntity.BlockDisplayEntity) {
                passenger.remove(reason);
            }
        });

        super.remove(reason);
    }


    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        return passenger.getPos();
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
