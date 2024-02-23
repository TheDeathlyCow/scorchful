package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
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

    public static final Item WARPED_LILY = new BlockItem(
            SBlocks.WARPED_LILY,
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

    public static final Item ROOTED_WARPED_NYLIUM = new BlockItem(
            SBlocks.ROOTED_WARPED_NYLIUM,
            new FabricItemSettings()
    );

    public static final Item SAND_PILE = new BlockItem(
            SBlocks.SAND_PILE,
            new FabricItemSettings()
    );

    public static final Item RED_SAND_PILE = new BlockItem(
            SBlocks.RED_SAND_PILE,
            new FabricItemSettings()
    );

    public static final Item TURTLE_CHESTPLATE = new ArmorItem(
            ScorchfulTurtleArmorMaterial.INSTANCE,
            ArmorItem.Type.CHESTPLATE,
            new FabricItemSettings()
    );

    public static final Item TURTLE_LEGGINGS = new ArmorItem(
            ScorchfulTurtleArmorMaterial.INSTANCE,
            ArmorItem.Type.LEGGINGS,
            new FabricItemSettings()
    );

    public static final Item TURTLE_BOOTS = new ArmorItem(
            ScorchfulTurtleArmorMaterial.INSTANCE,
            ArmorItem.Type.BOOTS,
            new FabricItemSettings()
    );

    public static void registerItems() {
        register("water_skin", WATER_SKIN);
        register("sun_hat", SUN_HAT);
        register("cactus_juice", CACTUS_JUICE);
        register("crimson_lily", CRIMSON_LILY);
        register("warped_lily", WARPED_LILY);
        register("rooted_netherrack", ROOTED_NETHERRACK);
        register("rooted_crimson_nylium", ROOTED_CRIMSON_NYLIUM);
        register("rooted_warped_nylium", ROOTED_WARPED_NYLIUM);
        register("sand_pile", SAND_PILE);
        register("red_sand_pile", RED_SAND_PILE);
        register("turtle_chestplate", TURTLE_CHESTPLATE);
        register("turtle_leggings", TURTLE_LEGGINGS);
        register("turtle_boots", TURTLE_BOOTS);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }
}
