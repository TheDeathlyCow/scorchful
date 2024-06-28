package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DesertVisionEntity extends Entity {

    private static final String VISION_TYPE_NBT_KEY = "vision_type";

    @Nullable
    private DesertVisionType visionType = null;

    private final List<Entity> children = new ArrayList<>();

    public DesertVisionEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    public DesertVisionType getVisionType() {
        return this.visionType;
    }

    public void setVision(PlayerEntity cause, @NotNull DesertVisionType visionType) {
        if (this.visionType != null) {
            throw new IllegalStateException("Desert vision type already set for " + this);
        }

        this.visionType = visionType;

        World world = this.getWorld();
        if (world.isClient) {
            return;
        }

        switch (this.visionType) {
            case DESERT_WELL -> this.spawnDesertWellVision(this, (ServerWorld) this.getWorld(), this.getBlockPos());
            case HUSK -> this.spawnHuskVision((ServerWorld) this.getWorld(), this.getBlockPos());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        this.children.forEach(Entity::discard);
        super.remove(reason);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        String visionName = nbt.getString(VISION_TYPE_NBT_KEY);
        try {
            this.visionType = DesertVisionType.valueOf(visionName);
        } catch (IllegalArgumentException e) {
            Scorchful.LOGGER.error("Unknown desert vision type: " + visionName);
            this.visionType = null;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.visionType != null) {
            nbt.putString(
                    VISION_TYPE_NBT_KEY,
                    this.visionType.toString()
            );
        }
    }

    private void spawnHuskVision(ServerWorld world, BlockPos pos) {
        HuskEntity entity = this.spawnAndRide(EntityType.HUSK, world, pos);
        if (entity != null) {
            entity.setInvulnerable(true);
            entity.setAiDisabled(true);
            entity.setNoGravity(true);
        }
    }

    private void spawnDesertWellVision(DesertVisionEntity desertVision, ServerWorld world, BlockPos blockPos) {
        blockPos = blockPos.down();
        final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
        final BlockState wall = Blocks.SANDSTONE.getDefaultState();

        // base
        for (int dy = -2; dy <= 0; ++dy) {
            for (int dx = -2; dx <= 2; ++dx) {
                for (int dz = -2; dz <= 2; ++dz) {
                    setBlockState(desertVision, world, blockPos.add(dx, dy, dz), wall);
                }
            }
        }

        // edges / rim
        for (int dx = -2; dx <= 2; ++dx) {
            for (int dz = -2; dz <= 2; ++dz) {
                if ((dx == -2 || dx == 2 || dz == -2 || dz == 2) && (dx != 0 && dz != 0)) {
                    setBlockState(desertVision, world, blockPos.add(dx, 1, dz), wall);
                }
            }
        }

        // slabs on rim
        setBlockState(desertVision, world, blockPos.add(2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(-2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, 2), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, -2), slab);

        // little hat
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), wall);
                } else {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), slab);
                }
            }
        }

        // corner pillars
        for (int j = 1; j <= 3; ++j) {
            setBlockState(desertVision, world, blockPos.add(-1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(-1, j, 1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, 1), wall);
        }
    }

    private void setBlockState(DesertVisionEntity parent, ServerWorld world, BlockPos pos, BlockState state) {

        if (!world.getBlockState(pos).isReplaceable()) {
            return;
        }

        DisplayEntity.BlockDisplayEntity entity = spawnAndRide(EntityType.BLOCK_DISPLAY, world, pos);
        if (entity != null) {
            ((BlockDisplayAccess) entity).scorchful$setBlockState(state);
        }
    }

    @Nullable
    private <T extends Entity> T spawnAndRide(EntityType<T> type, ServerWorld world, BlockPos pos) {
        T entity = type.create(world);
        if (entity != null) {
            ScorchfulComponents.ENTITY.get(entity).makeDesertVisionChild();
            this.children.add(entity);
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(entity);
        }
        return entity;
    }
}
