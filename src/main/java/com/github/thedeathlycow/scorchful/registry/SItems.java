package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.SArmorMaterials;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SItems {

    public static final Item WATER_SKIN = new WaterSkinItem(
            new FabricItemSettings()
                    .maxCount(1)
    );

    public static final Item STRAW_HAT = new ArmorItem(SArmorMaterials.STRAW, ArmorItem.Type.HELMET, new FabricItemSettings());

    public static void registerItems() {
        register("water_skin", WATER_SKIN);
        register("straw_hat", STRAW_HAT);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
