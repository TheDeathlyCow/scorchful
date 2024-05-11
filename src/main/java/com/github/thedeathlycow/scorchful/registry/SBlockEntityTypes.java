package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SBlockEntityTypes {



    public static void registerAll() {
    }

    public static void register(String name, BlockEntityType<?> type) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Scorchful.id(name), type);
    }

    private static <T extends BlockEntity> BlockEntityType<T> create(
            BlockEntityType.BlockEntityFactory<T> factory,
            Block... blocks
    ) {
        return BlockEntityType.Builder.create(factory, blocks).build();
    }

    private SBlockEntityTypes() {

    }

}
