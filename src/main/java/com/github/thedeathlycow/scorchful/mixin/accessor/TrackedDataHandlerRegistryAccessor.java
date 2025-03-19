package com.github.thedeathlycow.scorchful.mixin.accessor;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(TrackedDataHandlerRegistry.class)
public interface TrackedDataHandlerRegistryAccessor {
    @Accessor("OPTIONAL_BLOCK_STATE_CODEC")
    static PacketCodec<ByteBuf, Optional<BlockState>> scorchful$blockStatePackCodec() {
        throw new AssertionError();
    }
}
