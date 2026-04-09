package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevel;
import com.github.thedeathlycow.scorchful.item.component.HeatResistance;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceModifier;
import com.github.thedeathlycow.scorchful.item.enchantment.EnchantmentModifiers;
import com.github.thedeathlycow.scorchful.item.loot.TurtleScuteLootTableModifier;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public final class SItems {
    public static final Item WATER_SKIN = register(
            "water_skin",
            settings -> new WaterSkinItem(
                    settings
                            .stacksTo(1)
                            .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                            .component(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevel.HYDRATING)
            )
    );

    public static final Item SUN_HAT = register("sun_hat", SunHatItem::createItem);

    public static final Item CACTUS_JUICE = register(
            "cactus_juice",
            settings -> new Item(
                    settings.stacksTo(16)
                            .craftRemainder(Items.GLASS_BOTTLE)
                            .usingConvertsTo(Items.GLASS_BOTTLE)
                            .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevel.HYDRATING)
            )
    );

    public static final Item CRIMSON_LILY = register("crimson_lily", SBlocks.CRIMSON_LILY);

    public static final Item WARPED_LILY = register("warped_lily", SBlocks.WARPED_LILY);

    public static final Item ROOTED_NETHERRACK = register("rooted_netherrack", SBlocks.ROOTED_NETHERRACK);

    public static final Item ROOTED_CRIMSON_NYLIUM = register("rooted_crimson_nylium", SBlocks.ROOTED_CRIMSON_NYLIUM);

    public static final Item ROOTED_WARPED_NYLIUM = register("rooted_warped_nylium", SBlocks.ROOTED_WARPED_NYLIUM);

    public static final Item SAND_PILE = register("sand_pile", SBlocks.SAND_PILE);

    public static final Item RED_SAND_PILE = register("red_sand_pile", SBlocks.RED_SAND_PILE);

    public static final Item DUST = register("dust");
    public static final Item RED_DUST = register("red_dust");

    public static final Item TURTLE_CHESTPLATE = register(
            "turtle_chestplate",
            settings -> new Item(
                    settings
                            .humanoidArmor(SArmorMaterials.TURTLE, ArmorType.CHESTPLATE)
                            .durability(ArmorType.CHESTPLATE.getDurability(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_LEGGINGS = register(
            "turtle_leggings",
            settings -> new Item(
                    settings
                            .humanoidArmor(SArmorMaterials.TURTLE, ArmorType.LEGGINGS)
                            .durability(ArmorType.LEGGINGS.getDurability(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_BOOTS = register(
            "turtle_boots",
            settings -> new Item(
                    settings
                            .humanoidArmor(SArmorMaterials.TURTLE, ArmorType.BOOTS)
                            .durability(ArmorType.BOOTS.getDurability(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.VERY_PROTECTIVE)
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful items");
        UseItemCallback.EVENT.register(new FireChargeThrower());
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(DrinkLevel::applyToNewStack);
        ScorchfulItemEvents.CONSUME_ITEM.register((stack, player) -> {
            if (stack.is(SItemTags.IS_COOLING_FOOD)) {
                player.thermoo$addTemperature(
                        ScorchfulConfig.getTemperatureConfig().getFoodCooling(),
                        player.level().thermoo$temperatureSources().active()
                );
            }
        });
        HeatResistanceModifier.initialize();
        LootTableEvents.MODIFY.register(new TurtleScuteLootTableModifier());
        EnchantmentModifiers.initialize();
        TurtleArmorEffects.initialize();
    }

    private static Item register(String id) {
        return register(id, Item::new, new Item.Properties());
    }

    private static Item register(String id, Block block) {
        return register(id, settings -> new BlockItem(block, settings.useBlockDescriptionPrefix()));
    }

    private static Item register(String id, Function<Item.Properties, Item> itemFactory) {
        return register(id, itemFactory, new Item.Properties());
    }

    private static Item register(String id, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Scorchful.id(id));
        Item item = itemFactory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private SItems() {

    }
}
