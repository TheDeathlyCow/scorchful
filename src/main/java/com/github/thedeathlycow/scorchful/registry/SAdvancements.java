package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public final class SAdvancements {
    public static final ResourceKey<Advancement> DRINK_CACTUS_JUICE = key("adventure/drink_cactus_juice");
    public static final ResourceKey<Advancement> OBTAIN_TURTLE_ARMOR = key("husbandry/obtain_turtle_armor");
    public static final ResourceKey<Advancement> SHOOT_PARANOIA_ARROW = key("husbandry/shoot_paranoia_arrow");

    private static ResourceKey<Advancement> key(String path) {
        return ResourceKey.create(Registries.ADVANCEMENT, Scorchful.id(path));
    }

    private SAdvancements() {

    }
}