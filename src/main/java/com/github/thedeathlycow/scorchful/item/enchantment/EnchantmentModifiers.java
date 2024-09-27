package com.github.thedeathlycow.scorchful.item.enchantment;

import com.github.thedeathlycow.thermoo.api.predicate.SoakedLootCondition;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentSource;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.EntityTypeTags;

public class EnchantmentModifiers {

    public static void initialize() {
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyImpaling);
    }

    private static void modifyImpaling(RegistryKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.IMPALING) {
            return;
        }

        builder.addEffect(
                EnchantmentEffectComponentTypes.DAMAGE,
                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(2.5f)),
                AllOfLootCondition.builder(
                        () -> new SoakedLootCondition(
                                NumberRange.IntRange.atLeast(1),
                                NumberRange.DoubleRange.ANY
                        ),
                        InvertedLootCondition.builder(
                                EntityPropertiesLootCondition.builder(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.create()
                                                .type(EntityTypePredicate.create(EntityTypeTags.SENSITIVE_TO_IMPALING))
                                )
                        )
                )
        );
    }

    private EnchantmentModifiers() {

    }

}
