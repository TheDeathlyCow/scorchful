package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SItems {

    public static final Item WATER_SKIN = new WaterSkinItem(
            new FabricItemSettings()
                    .maxDamage(10)
    );

    public static void registerItems() {
        register("water_skin", WATER_SKIN);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
