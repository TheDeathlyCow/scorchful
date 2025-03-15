package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class SBlockEntityTypes {
    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful block entity types");
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            BlockEntityType.BlockEntityFactory<T> factory,
            Block... blocks
    ) {
        BlockEntityType<T> type = BlockEntityType.Builder.create(factory, blocks).build();
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Scorchful.id(name), type);
    }

    private SBlockEntityTypes() {

    }

}
