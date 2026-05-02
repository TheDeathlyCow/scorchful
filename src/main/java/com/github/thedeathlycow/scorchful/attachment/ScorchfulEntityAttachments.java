package com.github.thedeathlycow.scorchful.attachment;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ScorchfulEntityAttachments {
    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES,
            Scorchful.MODID
    );

    public static final Supplier<AttachmentType<RehydrationAttachment>> REHYDRATION = REGISTRY.register(
            "rehydration",
            () -> AttachmentType.serializable(RehydrationAttachment::new)
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerWaterAttachment>> PLAYER_WATER = REGISTRY.register(
            "player_water",
            () -> AttachmentType.serializable(PlayerWaterAttachment::new)
                    .build()
    );

    public static final Supplier<AttachmentType<EntityDesertVisionAttachment>> ENTITY_DESERT_VISION = REGISTRY.register(
            "entity_desert_vision",
            () -> AttachmentType.builder(EntityDesertVisionAttachment::new)
                    .sync(new EntityDesertVisionAttachment.SyncHandler())
                    .build()
    );
}
