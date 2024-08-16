package com.github.thedeathlycow.scorchful.item.loot;


import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class TurtleScuteLootTableModifier implements LootTableEvents.Modify {

    public static final RegistryKey<LootTable> EXTRA_SCUTE_IN_BURIED_TREASURE = registryKey("chests/extra_turtle_scute/buried_treasure");
    public static final RegistryKey<LootTable> EXTRA_SCUTE_IN_SHIPWRECK_SUPPLY = registryKey("chests/extra_turtle_scute/shipwreck_supply");
    public static final RegistryKey<LootTable> EXTRA_SCUTE_IN_SHIPWRECK_TREASURE = registryKey("chests/extra_turtle_scute/shipwreck_treasure");


    @Override
    public void modifyLootTable(
            RegistryKey<LootTable> key,
            LootTable.Builder tableBuilder,
            LootTableSource source,
            RegistryWrapper.WrapperLookup registries
    ) {
        if (!source.isBuiltin()) {
            return;
        }

        // could be better with a map implementation, but unnecessary here imo
        LootPool.Builder pool = null;

        if (key == LootTables.BURIED_TREASURE_CHEST) {
            pool = LootPool.builder()
                    .with(LootTableEntry.builder(EXTRA_SCUTE_IN_BURIED_TREASURE));
        } else if (key == LootTables.SHIPWRECK_SUPPLY_CHEST) {
            pool = LootPool.builder()
                    .with(LootTableEntry.builder(EXTRA_SCUTE_IN_SHIPWRECK_SUPPLY));
        } else if (key == LootTables.SHIPWRECK_TREASURE_CHEST) {
            pool = LootPool.builder()
                    .with(LootTableEntry.builder(EXTRA_SCUTE_IN_SHIPWRECK_TREASURE));
        }

        if (pool != null) {
            tableBuilder.pool(pool);
        }

    }

    private static RegistryKey<LootTable> registryKey(String name) {
        return RegistryKey.of(
                RegistryKeys.LOOT_TABLE,
                Scorchful.id(name)
        );
    }
}
