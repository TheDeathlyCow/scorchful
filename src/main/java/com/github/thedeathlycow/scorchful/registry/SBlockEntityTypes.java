package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SBlockEntityTypes {
    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful block entity types");
    }

//    public static <T extends BlockEntity> BlockEntityType<T> register(
//            String name,
//            BlockEntityType.BlockEntitySupplier<T> factory,
//            Block... blocks
//    ) {
//        BlockEntityType<T> type = BlockEntityType.Builder.of(factory, blocks).build();
//        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Scorchful.id(name), type);
//    }

    private SBlockEntityTypes() {

    }

}
