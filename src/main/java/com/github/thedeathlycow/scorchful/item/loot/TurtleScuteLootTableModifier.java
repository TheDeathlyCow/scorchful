package com.github.thedeathlycow.scorchful.item.loot;


import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class TurtleScuteLootTableModifier implements LootTableEvents.Modify {
    public static final ResourceKey<LootTable> EXTRA_SCUTE_IN_BURIED_TREASURE = registryKey("chests/extra_turtle_scute/buried_treasure");
    public static final ResourceKey<LootTable> EXTRA_SCUTE_IN_SHIPWRECK_SUPPLY = registryKey("chests/extra_turtle_scute/shipwreck_supply");
    public static final ResourceKey<LootTable> EXTRA_SCUTE_IN_SHIPWRECK_TREASURE = registryKey("chests/extra_turtle_scute/shipwreck_treasure");


    @Override
    public void modifyLootTable(
            ResourceKey<LootTable> key,
            LootTable.Builder tableBuilder,
            LootTableSource source,
            HolderLookup.Provider registries
    ) {
        if (!source.isBuiltin()) {
            return;
        }

        // could be better with a map implementation, but unnecessary here imo
        LootPool.Builder pool = null;

        if (key == BuiltInLootTables.BURIED_TREASURE) {
            pool = LootPool.lootPool()
                    .add(NestedLootTable.lootTableReference(EXTRA_SCUTE_IN_BURIED_TREASURE));
        } else if (key == BuiltInLootTables.SHIPWRECK_SUPPLY) {
            pool = LootPool.lootPool()
                    .add(NestedLootTable.lootTableReference(EXTRA_SCUTE_IN_SHIPWRECK_SUPPLY));
        } else if (key == BuiltInLootTables.SHIPWRECK_TREASURE) {
            pool = LootPool.lootPool()
                    .add(NestedLootTable.lootTableReference(EXTRA_SCUTE_IN_SHIPWRECK_TREASURE));
        }

        if (pool != null) {
            tableBuilder.withPool(pool);
        }
    }

    private static ResourceKey<LootTable> registryKey(String name) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                Scorchful.id(name)
        );
    }
}
