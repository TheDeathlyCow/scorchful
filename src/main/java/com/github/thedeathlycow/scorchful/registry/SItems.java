package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.SArmorMaterials;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SItems {

    public static final Item WATER_SKIN = new WaterSkinItem(
            new FabricItemSettings()
                    .maxCount(1)
    );

    public static final Item SUN_HAT = new SunHatItem(
            new FabricItemSettings()
                    .equipmentSlot(stack -> EquipmentSlot.HEAD)
                    .maxCount(1)
    );

    public static void registerItems() {
        register("water_skin", WATER_SKIN);
        register("sun_hat", SUN_HAT);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
