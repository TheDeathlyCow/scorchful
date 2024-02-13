package com.github.thedeathlycow.scorchful.enchantment;

import com.github.thedeathlycow.scorchful.registry.SEnchantments;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class RehydrationEnchantment extends Enchantment {

    public RehydrationEnchantment(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.ARMOR, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return level * 25;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    public static void addToNetherLoot() {
        var piglinBarterLoot = new Identifier("gameplay/piglin_bartering");
        LootTableEvents.MODIFY.register(
                (resourceManager, lootManager, id, tableBuilder, source) -> {
                    if (source != LootTableSource.DATA_PACK && id.equals(piglinBarterLoot)) {
                        tableBuilder.pool(
                                LootPool.builder()
                                        .rolls(ConstantLootNumberProvider.create(1f))
                                        .with(ItemEntry.builder(Items.ENCHANTED_BOOK).weight(5))
                                        .apply(
                                                EnchantRandomlyLootFunction.create()
                                                        .add(SEnchantments.REHYDRATION)
                                                        .build()
                                        )
                                        .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE).weight(25))
                                        .apply(
                                                EnchantRandomlyLootFunction.create()
                                                        .add(SEnchantments.REHYDRATION)
                                                        .build()
                                        )
                                        .with(EmptyEntry.builder().weight(429))
                        );
                    }
                }
        );
    }

    private static void addToLoot(Identifier lootTable, float chance) {
        LootTableEvents.MODIFY.register(
                (resourceManager, lootManager, id, tableBuilder, source) -> {
                    if (source != LootTableSource.DATA_PACK && id.equals(lootTable)) {
                        tableBuilder.pool(makeBuilder(chance));
                    }
                }
        );
    }

    private static LootPool.Builder makeBuilder(float chance) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1f))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(Items.ENCHANTED_BOOK))
                .apply(
                        EnchantRandomlyLootFunction.create()
                                .add(SEnchantments.REHYDRATION)
                                .build()
                );
    }
}
