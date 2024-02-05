package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class SSoundEvents {

    public static final SoundEvent ITEM_WATER_SKIN_FILL = create("item.scorchful.water_skin.fill");
    public static final SoundEvent TEMPERATURE_EFFECT_HEARTBEAT = create("temperature_effect.scorchful.heartbeat");
    public static final SoundEvent REHYDRATE = create("enchantment.scorchful.rehydration");
    public static final SoundEvent CRIMSON_LILY_SQUELCH = create("block.scorchful.crimson_lily.squelch");
    public static void registerAll() {
        register(ITEM_WATER_SKIN_FILL);
        register(TEMPERATURE_EFFECT_HEARTBEAT);
        register(REHYDRATE);
        register(CRIMSON_LILY_SQUELCH);
    }

    private static void register(SoundEvent event) {
        Registry.register(Registries.SOUND_EVENT, event.getId(), event);
    }

    private static SoundEvent create(String name) {
        return SoundEvent.of(Scorchful.id(name));
    }

    private SSoundEvents() {

    }
}
