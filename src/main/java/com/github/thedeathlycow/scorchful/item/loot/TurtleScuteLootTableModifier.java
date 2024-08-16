package com.github.thedeathlycow.scorchful.item.loot;


import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class TurtleScuteLootTableModifier implements LootTableEvents.Modify {

    public static final RegistryKey<LootTable> EXTRA_TURTLE_SCUTE_CHEST = RegistryKey.of(
            RegistryKeys.LOOT_TABLE,
            Scorchful.id("chests/extra_turtle_scute")
    );



    @Override
    public void modifyLootTable(
            RegistryKey<LootTable> key,
            LootTable.Builder tableBuilder,
            LootTableSource source,
            RegistryWrapper.WrapperLookup registries
    ) {
        if (key == LootTables.BURIED_TREASURE_CHEST && source.isBuiltin()) {
            LootPool.Builder pool = LootPool.builder()
                    .with(LootTableEntry.builder(EXTRA_TURTLE_SCUTE_CHEST));

            tableBuilder.pool(pool);
        }


    }
}
