package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.advancement.Advancement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class SAdvancements {
    public static final RegistryKey<Advancement> DRINK_CACTUS_JUICE = key("adventure/drink_cactus_juice");
    public static final RegistryKey<Advancement> OBTAIN_TURTLE_ARMOR = key("husbandry/obtain_turtle_armor");
    public static final RegistryKey<Advancement> SHOOT_PARANOIA_ARROW = key("husbandry/shoot_paranoia_arrow");

    private static RegistryKey<Advancement> key(String path) {
        return RegistryKey.of(RegistryKeys.ADVANCEMENT, Scorchful.id(path));
    }

    private SAdvancements() {

    }
}