package com.github.thedeathlycow.scorchful.item.loot;

import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

public final class HuskLootModifier implements LootTableEvents.Modify {
    @Nullable
    private final ResourceKey<LootTable> lootTableKey;

    public HuskLootModifier(EntityType<?> entityType) {
        this.lootTableKey = entityType.getDefaultLootTable().orElse(null);
    }

    @Override
    public void modifyLootTable(ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider holder) {
        if (source.isBuiltin() && key == this.lootTableKey) {
            LootPool.Builder builder = LootPool.lootPool()
                    .setRolls(UniformGenerator.between(0f, 2f))
                    .add(
                            LootItem.lootTableItem(SItems.DUST)
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(holder, UniformGenerator.between(0.0f, 1.0f)))
                    );

            tableBuilder.withPool(builder);
        }
    }
}