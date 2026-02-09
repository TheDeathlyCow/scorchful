package com.github.thedeathlycow.scorchful.item.enchantment;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ItemConfig;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.predicate.SoakedLootCondition;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentSource;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class EnchantmentModifiers {

    public static void initialize() {
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyFireProtection);
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyImpaling);
    }

    private static void modifyImpaling(ResourceKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.IMPALING) {
            return;
        }

        ItemConfig config = ScorchfulConfig.getItemConfig();

        builder.withEffect(
                EnchantmentEffectComponents.DAMAGE,
                new AddValue(LevelBasedValue.perLevel(config.getImpalingDamagePerLevel())),
                SoakedLootCondition.builder(MinMaxBounds.Ints.atLeast(1))
                // TODO: registry lookup doesnt work, throws: java.lang.IllegalStateException: Missing tag TagKey[minecraft:entity_type / minecraft:sensitive_to_impaling]
//                AllOfLootCondition.builder(
//                        () -> new SoakedLootCondition(
//                                ,
//                                NumberRange.DoubleRange.ANY
//                        ),
//                        InvertedLootCondition.builder(
//                                EntityPropertiesLootCondition.builder(
//                                        LootContext.EntityTarget.THIS,
//                                        EntityPredicate.Builder.create()
//                                                .type(EntityTypePredicate.create(
//                                                        Registries.ENTITY_TYPE,
//                                                        EntityTypeTags.SENSITIVE_TO_IMPALING
//                                                ))
//                                )
//                        )
//                )
        );
    }

    private static void modifyFireProtection(ResourceKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.FIRE_PROTECTION) {
            return;
        }

        double valuePerLevel = ScorchfulConfig.getItemConfig().getFireProtectionHeatResistancePerLevel();
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
