package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceModifier;
import com.github.thedeathlycow.scorchful.item.enchantment.EnchantmentModifiers;
import com.github.thedeathlycow.scorchful.item.loot.TurtleScuteLootTableModifier;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public final class SItems {
    public static final Item WATER_SKIN = register(
            "water_skin",
            settings -> new WaterSkinItem(
                    settings
                            .maxCount(1)
                            .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
                            .component(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING)
            )
    );

    public static final Item SUN_HAT = register("sun_hat", SunHatItem::createItem);

    public static final Item CACTUS_JUICE = register(
            "cactus_juice",
            settings -> new Item(
                    settings.maxCount(16)
                            .recipeRemainder(Items.GLASS_BOTTLE)
                            .useRemainder(Items.GLASS_BOTTLE)
                            .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
                            .component(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING)
            )
    );

    public static final Item CRIMSON_LILY = register("crimson_lily", SBlocks.CRIMSON_LILY);

    public static final Item WARPED_LILY = register("warped_lily", SBlocks.WARPED_LILY);

    public static final Item ROOTED_NETHERRACK = register("rooted_netherrack", SBlocks.ROOTED_NETHERRACK);

    public static final Item ROOTED_CRIMSON_NYLIUM = register("rooted_crimson_nylium", SBlocks.ROOTED_CRIMSON_NYLIUM);

    public static final Item ROOTED_WARPED_NYLIUM = register("rooted_warped_nylium", SBlocks.ROOTED_WARPED_NYLIUM);

    public static final Item SAND_PILE = register("sand_pile", SBlocks.SAND_PILE);

    public static final Item RED_SAND_PILE = register("red_sand_pile", SBlocks.RED_SAND_PILE);

    public static final Item TURTLE_CHESTPLATE = register(
            "turtle_chestplate",
            settings -> new Item(
                    settings
                            .armor(SArmorMaterials.TURTLE, EquipmentType.CHESTPLATE)
                            .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_LEGGINGS = register(
            "turtle_leggings",
            settings -> new Item(
                    settings
                            .armor(SArmorMaterials.TURTLE, EquipmentType.LEGGINGS)
                            .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static final Item TURTLE_BOOTS = register(
            "turtle_boots",
            settings -> new Item(
                    settings
                            .armor(SArmorMaterials.TURTLE, EquipmentType.BOOTS)
                            .maxDamage(EquipmentType.BOOTS.getMaxDamage(25))
                            .component(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE)
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful items");
        UseItemCallback.EVENT.register(new FireChargeThrower());
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(DrinkLevelComponent::applyToNewStack);
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
        TurtleArmorEffects.initialize();
    }

    private static Item register(String id, Block block) {
        return register(id, settings -> new BlockItem(block, settings.useBlockPrefixedTranslationKey()));
    }

    private static Item register(String id, Function<Item.Settings, Item> itemFactory) {
        return register(id, itemFactory, new Item.Settings());
    }

    private static Item register(String id, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Scorchful.id(id));
        Item item = itemFactory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    private SItems() {

    }
}
