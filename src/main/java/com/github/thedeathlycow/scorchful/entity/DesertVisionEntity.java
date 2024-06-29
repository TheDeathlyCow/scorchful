package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.event.DesertVisionActivation;
import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import java.util.Optional;
import java.util.UUID;

public class DesertVisionEntity extends Entity {

    private static final String VISION_TYPE_NBT_KEY = "vision_type";
    private static final String TTL_NBT_KEY = "time_to_live";
    private static final String CAUSE_NBT_KEY = "causing_player";

    private static final double ACTIVATION_DISTANCE = 8.0;

    private final List<Entity> children = new ArrayList<>();

    private static final TrackedData<Optional<UUID>> CAUSE = DataTracker.registerData(
            DesertVisionEntity.class,
            TrackedDataHandlerRegistry.OPTIONAL_UUID
    );

    private static final TrackedData<Integer> VISION_TYPE_INDEX = DataTracker.registerData(
            DesertVisionEntity.class,
            TrackedDataHandlerRegistry.INTEGER
    );

    private int timeToLive = 10 * 20; // 10 seconds

    @Nullable
    private PlayerEntity cachedCause;

    public DesertVisionEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(CAUSE, Optional.empty());
        this.dataTracker.startTracking(VISION_TYPE_INDEX, -1);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Nullable
    public DesertVisionType getVisionType() {
        int index = this.dataTracker.get(VISION_TYPE_INDEX);
        return index >= 0
                ? DesertVisionType.values()[index]
                : null;
    }

    private void setVisionType(@Nullable DesertVisionType type) {
        if (type == null) {
            this.dataTracker.set(VISION_TYPE_INDEX, -1);
        } else {
            this.dataTracker.set(VISION_TYPE_INDEX, type.ordinal());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.tickCanLive()) {
            this.discard();
        }
    }

    @Nullable
    public PlayerEntity getCause() {
        Optional<UUID> causeID = this.dataTracker.get(CAUSE);
        if (causeID.isPresent() && this.cachedCause == null) {
            this.cachedCause = this.getWorld().getPlayerByUuid(causeID.get());
        }
        return this.cachedCause;
    }

    public boolean causeHasHeatStroke() {
        PlayerEntity cause = this.getCause();
        return cause != null && cause.hasStatusEffect(SStatusEffects.HEAT_STROKE);
    }

    public void setVision(PlayerEntity cause, @NotNull DesertVisionType visionType) {
        if (this.getVisionType() != null) {
            throw new IllegalStateException("Desert vision type already set for " + this);
        }
        World world = this.getWorld();
        if (world.isClient) {
            return;
        }
        this.dataTracker.set(CAUSE, Optional.of(cause.getUuid()));
        this.setVisionType(visionType);

        switch (visionType) {
            case DESERT_WELL -> this.spawnDesertWellVision((ServerWorld) this.getWorld(), this.getBlockPos());
            case HUSK -> this.spawnHuskVision((ServerWorld) this.getWorld(), this.getBlockPos());
            case POPPY -> this.spawnPoppyVision((ServerWorld) this.getWorld(), this.getBlockPos());
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
            this.setVisionType(DesertVisionType.valueOf(visionName));
        } catch (IllegalArgumentException e) {
            Scorchful.LOGGER.error("Unknown desert vision type: " + visionName);
            this.setVisionType(null);
        }

        this.timeToLive = nbt.getInt(TTL_NBT_KEY);

        if (nbt.containsUuid(CAUSE_NBT_KEY)) {
            this.dataTracker.set(CAUSE, Optional.of(nbt.getUuid(CAUSE_NBT_KEY)));
        } else {
            this.dataTracker.set(CAUSE, Optional.empty());
        }
        this.cachedCause = null;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        DesertVisionType visionType = this.getVisionType();
        if (visionType != null) {
            nbt.putString(
                    VISION_TYPE_NBT_KEY,
                    visionType.toString()
            );
        }

        nbt.putInt(TTL_NBT_KEY, this.timeToLive);
        Optional<UUID> cause = this.dataTracker.get(CAUSE);
        cause.ifPresent(uuid -> nbt.putUuid(CAUSE_NBT_KEY, uuid));
    }

    private boolean tickCanLive() {
        Optional<UUID> cause = this.dataTracker.get(CAUSE);
        if (!this.getWorld().isClient && this.timeToLive-- <= 0) {
            return false;
        } else if (cause.isPresent()) {
            PlayerEntity causeEntity = this.getCause();
            if (causeEntity != null) {
                if (this.squaredDistanceTo(causeEntity) < ACTIVATION_DISTANCE * ACTIVATION_DISTANCE) {
                    DesertVisionActivation.EVENT.invoker().onActivated(this, causeEntity);
                    return false;
                }
                return causeEntity.hasStatusEffect(SStatusEffects.HEAT_STROKE);
            }
        }
        return true;
    }

    private void spawnPoppyVision(ServerWorld world, BlockPos pos) {
        setBlockState(world, pos, Blocks.POPPY.getDefaultState());
    }

    private void spawnHuskVision(ServerWorld world, BlockPos pos) {
        HuskEntity entity = this.spawnAndRide(EntityType.HUSK, world, pos);
        if (entity != null) {
            entity.setInvulnerable(true);
            entity.setAiDisabled(true);
            entity.setNoGravity(true);
        }
    }

    private void spawnDesertWellVision(ServerWorld world, BlockPos blockPos) {
        blockPos = blockPos.down();
        final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
        final BlockState wall = Blocks.SANDSTONE.getDefaultState();

        // base
        for (int dy = -2; dy <= 0; ++dy) {
            for (int dx = -2; dx <= 2; ++dx) {
                for (int dz = -2; dz <= 2; ++dz) {
                    setBlockState(world, blockPos.add(dx, dy, dz), wall);
                }
            }
        }

        // edges / rim
        for (int dx = -2; dx <= 2; ++dx) {
            for (int dz = -2; dz <= 2; ++dz) {
                if ((dx == -2 || dx == 2 || dz == -2 || dz == 2) && (dx != 0 && dz != 0)) {
                    setBlockState(world, blockPos.add(dx, 1, dz), wall);
                }
            }
        }

        // slabs on rim
        setBlockState(world, blockPos.add(2, 1, 0), slab);
        setBlockState(world, blockPos.add(-2, 1, 0), slab);
        setBlockState(world, blockPos.add(0, 1, 2), slab);
        setBlockState(world, blockPos.add(0, 1, -2), slab);

        // little hat
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) {
                    setBlockState(world, blockPos.add(j, 4, k), wall);
                } else {
                    setBlockState(world, blockPos.add(j, 4, k), slab);
                }
            }
        }

        // corner pillars
        for (int j = 1; j <= 3; ++j) {
            setBlockState(world, blockPos.add(-1, j, -1), wall);
            setBlockState(world, blockPos.add(-1, j, 1), wall);
            setBlockState(world, blockPos.add(1, j, -1), wall);
            setBlockState(world, blockPos.add(1, j, 1), wall);
        }
    }

    private void setBlockState(ServerWorld world, BlockPos pos, BlockState state) {

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
            ScorchfulComponents.DESERT_VISION_CHILD.get(entity).makeDesertVisionChild(this.getCause());
            ScorchfulComponents.DESERT_VISION_CHILD.sync(entity);

            this.children.add(entity);
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            if (!world.spawnEntity(entity)) {
                return null;
            }
        }
        return entity;
    }
}
