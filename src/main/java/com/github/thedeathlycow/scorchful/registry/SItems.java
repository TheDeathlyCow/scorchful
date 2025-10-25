package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.*;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceModifier;
import com.github.thedeathlycow.scorchful.item.enchantment.EnchantmentModifiers;
import com.github.thedeathlycow.scorchful.item.loot.TurtleScuteLootTableModifier;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Function;

public final class SItems {
    public static final Item WATER_SKIN = register(
            "water_skin",
            settings -> new WaterSkinItem(
                    settings
                            .maxCount(1)
                            .component(SDataComponentTypes.NUM_DRINKS, 0)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING)
            )
    );

    public static final Item SUN_HAT = register(
            "sun_hat",
            settings -> new SunHatItem(
                    settings
                            .equipmentSlot((entity, stack) -> EquipmentSlot.HEAD)
                            .attributeModifiers(SunHatItem.attributeModifiers())
                            .maxCount(1)
            )
    );

    public static final Item CACTUS_JUICE = register(
            "cactus_juice",
            settings -> new SingleDrinkItem(
                    settings
                            .maxCount(16)
                            .recipeRemainder(Items.GLASS_BOTTLE)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING),
                    Items.GLASS_BOTTLE::getDefaultStack
            )
    );

    public static final Item CRIMSON_LILY = register(
            "crimson_lily",
            settings -> new BlockItem(SBlocks.CRIMSON_LILY, settings)
    );

    public static final Item WARPED_LILY = register(
            "warped_lily",
            settings -> new BlockItem(SBlocks.WARPED_LILY, settings)
    );

    public static final Item ROOTED_NETHERRACK = register(
            "rooted_netherrack",
            settings -> new BlockItem(SBlocks.ROOTED_NETHERRACK, settings)
    );

    public static final Item ROOTED_CRIMSON_NYLIUM = register(
            "rooted_crimson_nylium",
            settings -> new BlockItem(SBlocks.ROOTED_CRIMSON_NYLIUM, settings)
    );

    public static final Item ROOTED_WARPED_NYLIUM = register(
            "rooted_warped_nylium",
            settings -> new BlockItem(SBlocks.ROOTED_WARPED_NYLIUM, settings)
    );

    public static final Item SAND_PILE = register(
            "sand_pile",
            settings -> new BlockItem(SBlocks.SAND_PILE, settings)
    );

    public static final Item RED_SAND_PILE = register(
            "red_sand_pile",
            settings -> new BlockItem(SBlocks.RED_SAND_PILE, settings)
    );

    public static final Item TURTLE_CHESTPLATE = register(
            "turtle_chestplate",
            settings -> new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.CHESTPLATE,
                    settings
                            .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_LEGGINGS = register(
            "turtle_leggings",
            settings -> new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.LEGGINGS,
                    settings
                            .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_BOOTS = register(
            "turtle_boots",
            settings -> new ArmorItem(
                    SArmorMaterials.TURTLE,
                    ArmorItem.Type.BOOTS,
                    settings
                            .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful items");
        UseItemCallback.EVENT.register(new FireChargeThrower());
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(DrinkLevelComponent::applyToNewStack);
        ScorchfulItemEvents.CONSUME_ITEM.register(DrinkItem::applyWater);
        ScorchfulItemEvents.CONSUME_ITEM.register((stack, player) -> {
            if (stack.isIn(SItemTags.IS_COOLING_FOOD)) {
                player.thermoo$addTemperature(
                        Scorchful.getConfig().heatingConfig.getTemperatureFromCoolingFood(),
                        HeatingModes.ACTIVE
                );
            }
        });
        HeatResistanceModifier.initialize();
        LootTableEvents.MODIFY.register(new TurtleScuteLootTableModifier());
        EnchantmentModifiers.initialize();
    }

    public static Item register(String id, Function<Item.Settings, Item> itemFactory) {
        return register(id, itemFactory, new Item.Settings());
    }

    public static Item register(String id, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        Item item = itemFactory.apply(settings);

        return Registry.register(Registries.ITEM, Scorchful.id(id), item);
    }

    private SItems() {

    }
}
