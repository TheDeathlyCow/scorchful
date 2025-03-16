package com.github.thedeathlycow.scorchful.item.enchantment;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.predicate.SoakedLootCondition;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentSource;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.EntityTypeTags;

public class EnchantmentModifiers {

    public static void initialize() {
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyFireProtection);
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyImpaling);
    }

    private static void modifyImpaling(RegistryKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.IMPALING) {
            return;
        }

        CombatConfig config = Scorchful.getConfig().combatConfig;
        RegistryEntryLookup<EntityType<?>> entityLookup = DynamicRegistryManager.of(Registries.REGISTRIES)
                .getOrThrow(RegistryKeys.ENTITY_TYPE);

        builder.addEffect(
                EnchantmentEffectComponentTypes.DAMAGE,
                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(config.getImpalingDamagePerLevel())),
                AllOfLootCondition.builder(
                        () -> new SoakedLootCondition(
                                NumberRange.IntRange.atLeast(1),
                                NumberRange.DoubleRange.ANY
                        ),
                        InvertedLootCondition.builder(
                                EntityPropertiesLootCondition.builder(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.create()
                                                .type(EntityTypePredicate.create(
                                                        entityLookup,
                                                        EntityTypeTags.SENSITIVE_TO_IMPALING
                                                ))
                                )
                        )
                )
        );
    }

    private static void modifyFireProtection(RegistryKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.FIRE_PROTECTION) {
            return;
        }

        double valuePerLevel = Scorchful.getConfig().combatConfig.getFireProtectionHeatResistancePerLevel();
        builder.addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new AttributeEnchantmentEffect(
                        Scorchful.id("enchantment.fire_protection.heat_resistance"),
                        ThermooAttributes.HEAT_RESISTANCE,
                        EnchantmentLevelBasedValue.linear((float) valuePerLevel),
                        EntityAttributeModifier.Operation.ADD_VALUE
                )
        );
    }

    private EnchantmentModifiers() {

    }
}
