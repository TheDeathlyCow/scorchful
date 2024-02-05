package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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

    public static final Item CACTUS_JUICE = new SingleDrinkItem(
            new FabricItemSettings()
                    .maxCount(16)
                    .recipeRemainder(Items.GLASS_BOTTLE),
            Items.GLASS_BOTTLE::getDefaultStack
    );

    public static final Item CRIMSON_LILY = new BlockItem(
            SBlocks.CRIMSON_LILY,
            new FabricItemSettings()
    );

    public static final Item ROOTED_NETHERRACK = new BlockItem(
            SBlocks.ROOTED_NETHERRACK,
            new FabricItemSettings()
    );

    public static final Item ROOTED_CRIMSON_NYLIUM = new BlockItem(
            SBlocks.ROOTED_CRIMSON_NYLIUM,
            new FabricItemSettings()
    );


    public static void registerItems() {
        register("water_skin", WATER_SKIN);
        register("sun_hat", SUN_HAT);
        register("cactus_juice", CACTUS_JUICE);
        register("crimson_lily", CRIMSON_LILY);
        register("rooted_netherrack", ROOTED_NETHERRACK);
        register("rooted_crimson_nylium", ROOTED_CRIMSON_NYLIUM);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
