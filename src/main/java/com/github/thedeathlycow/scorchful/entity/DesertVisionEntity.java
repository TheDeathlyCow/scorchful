package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DesertVisionEntity extends Entity {

    @Nullable
    private DesertVisionType visionType = null;

    private static final String VISION_TYPE_NBT_KEY = "vision_type";

    public DesertVisionEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setVision(PlayerEntity cause, DesertVisionType visionType) {
        if (this.visionType != null) {
            throw new IllegalStateException("Desert vision type already set for " + this);
        }

        this.visionType = visionType;

        World world = this.getWorld();
        if (world.isClient) {
            return;
        }

        if (this.visionType == DesertVisionType.DESERT_WELL) {
            spawnFakeDesertWell(this, (ServerWorld) this.getWorld(), this.getBlockPos());
        }
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

        this.visionType = Util.getResult(
                DesertVisionType.CODEC.decode(NbtOps.INSTANCE, nbt.get(VISION_TYPE_NBT_KEY)),
                NbtException::new
        ).getFirst();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put(
                VISION_TYPE_NBT_KEY,
                Util.getResult(
                        DesertVisionType.CODEC.encode(
                                this.visionType,
                                NbtOps.INSTANCE,
                                nbt
                        ),
                        NbtException::new
                )
        );
    }

    private static void spawnFakeDesertWell(DesertVisionEntity desertVision, ServerWorld world, BlockPos blockPos) {
        final BlockState sand = Blocks.SAND.getDefaultState();
        final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
        final BlockState wall = Blocks.SANDSTONE.getDefaultState();
        final BlockState fluidInside = Blocks.WATER.getDefaultState();

        for (int i = -2; i <= 0; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -2; k <= 2; ++k) {
                    setBlockState(desertVision, world, blockPos.add(j, i, k), wall);
                }
            }
        }

        setBlockState(desertVision, world, blockPos, fluidInside);

        for (Direction direction : Direction.Type.HORIZONTAL) {
            setBlockState(desertVision, world, blockPos.offset(direction), fluidInside);
        }

        BlockPos blockPos2 = blockPos.down();
        setBlockState(desertVision, world, blockPos2, sand);

        for (Direction direction2 : Direction.Type.HORIZONTAL) {
            setBlockState(desertVision, world, blockPos2.offset(direction2), sand);
        }

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                if (j == -2 || j == 2 || k == -2 || k == 2) {
                    setBlockState(desertVision, world, blockPos.add(j, 1, k), wall);
                }
            }
        }

        setBlockState(desertVision, world, blockPos.add(2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(-2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, 2), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, -2), slab);

        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), wall);
                } else {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), slab);
                }
            }
        }

        for (int j = 1; j <= 3; ++j) {
            setBlockState(desertVision, world, blockPos.add(-1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(-1, j, 1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, 1), wall);
        }
    }

    private static void setBlockState(DesertVisionEntity parent, ServerWorld world, BlockPos pos, BlockState state) {
        var entity = EntityType.BLOCK_DISPLAY.create(world);
        if (entity != null) {
            ((BlockDisplayAccess) entity).scorchful$setBlockState(state);
            entity.startRiding(parent, true);
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(entity);
        }
    }
}
