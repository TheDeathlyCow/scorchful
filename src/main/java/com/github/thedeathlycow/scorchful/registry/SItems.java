package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSatchelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SItems {

    public static final Item WATER_SATCHEL = new WaterSatchelItem(
            new FabricItemSettings()
                    .maxDamage(10)
    );

    public static void registerItems() {
        register("water_satchel", WATER_SATCHEL);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
