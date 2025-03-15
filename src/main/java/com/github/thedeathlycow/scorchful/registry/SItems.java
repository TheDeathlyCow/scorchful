package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.SingleDrinkItem;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class SItems {
    public static final Item WATER_SKIN = register(
            "water_skin",
            new WaterSkinItem(
                    new Item.Settings()
                            .maxCount(1)
                            .component(SDataComponentTypes.NUM_DRINKS, 0)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING)
            )
    );

    public static final Item SUN_HAT = register(
            "sun_hat",
            new SunHatItem(
                    new Item.Settings()
                            .equipmentSlot((entity, stack) -> EquipmentSlot.HEAD)
                            .attributeModifiers(SunHatItem.attributeModifiers())
                            .maxCount(1)
            )
    );

    public static final Item CACTUS_JUICE = register(
            "cactus_juice",
            new SingleDrinkItem(
                    new Item.Settings()
                            .maxCount(16)
                            .recipeRemainder(Items.GLASS_BOTTLE)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING),
                    Items.GLASS_BOTTLE::getDefaultStack
            )
    );

    public static final Item CRIMSON_LILY = register(
            "crimson_lily",
            new BlockItem(
                    SBlocks.CRIMSON_LILY,
                    new Item.Settings()
            )
    );

    public static final Item WARPED_LILY = register(
            "warped_lily",
            new BlockItem(
                    SBlocks.WARPED_LILY,
                    new Item.Settings()
            )
    );

    public static final Item ROOTED_NETHERRACK = register(
            "rooted_netherrack",
            new BlockItem(
                    SBlocks.ROOTED_NETHERRACK,
                    new Item.Settings()
            )
    );

    public static final Item ROOTED_CRIMSON_NYLIUM = register(
            "rooted_crimson_nylium",
            new BlockItem(
                    SBlocks.ROOTED_CRIMSON_NYLIUM,
                    new Item.Settings()
            )
    );

    public static final Item ROOTED_WARPED_NYLIUM = register(
            "rooted_warped_nylium",
            new BlockItem(
                    SBlocks.ROOTED_WARPED_NYLIUM,
                    new Item.Settings()
            )
    );

    public static final Item SAND_PILE = register(
            "sand_pile",
            new BlockItem(
                    SBlocks.SAND_PILE,
                    new Item.Settings()
            )
    );

    public static final Item RED_SAND_PILE = register(
            "red_sand_pile",
            new BlockItem(
                    SBlocks.RED_SAND_PILE,
                    new Item.Settings()
            )
    );

    public static final Item TURTLE_CHESTPLATE = register(
            "turtle_chestplate",
            new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_LEGGINGS = register(
            "turtle_leggings",
            new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_BOOTS = register(
            "turtle_boots",
            new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful items");
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }

    private SItems() {

    }
}
