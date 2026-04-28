package com.github.thedeathlycow.scorchful.item.enchantment;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.predicate.SoakedLootCondition;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentSource;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class EnchantmentModifiers {

    public static void initialize() {
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyFireProtection);
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyImpaling);
    }

    private static void modifyImpaling(ResourceKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.IMPALING) {
            return;
        }

        CombatConfig config = Scorchful.getConfig().combatConfig;
        builder.withEffect(
                EnchantmentEffectComponents.DAMAGE,
                new AddValue(LevelBasedValue.perLevel(config.getImpalingDamagePerLevel())),
                AllOfCondition.allOf(
                        () -> new SoakedLootCondition(
                                MinMaxBounds.Ints.atLeast(1),
                                MinMaxBounds.Doubles.ANY
                        ),
                        InvertedLootItemCondition.invert(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .entityType(EntityTypePredicate.of(EntityTypeTags.SENSITIVE_TO_IMPALING))
                                )
                        )
                )
        );
    }

    private static void modifyFireProtection(ResourceKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.FIRE_PROTECTION) {
            return;
        }

        double valuePerLevel = Scorchful.getConfig().combatConfig.getFireProtectionHeatResistancePerLevel();
        builder.withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                        Scorchful.id("enchantment.fire_protection.heat_resistance"),
                        ThermooAttributes.HEAT_RESISTANCE,
                        LevelBasedValue.perLevel((float) valuePerLevel),
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }

    private EnchantmentModifiers() {

    }
}
