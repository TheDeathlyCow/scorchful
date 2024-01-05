package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class SSoundEvents {

    public static final SoundEvent ITEM_WATER_SKIN_FILL = create("item.scorchful.water_skin.fill");

    public static void registerAll() {
        register(ITEM_WATER_SKIN_FILL);
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
