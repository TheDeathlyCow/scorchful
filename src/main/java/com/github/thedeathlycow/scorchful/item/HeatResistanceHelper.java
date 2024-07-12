package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class HeatResistanceHelper {

    public static double getHeatResistance(RegistryEntry<ArmorMaterial> armorMaterial, ArmorItem.Type armorType) {
        CombatConfig config = Scorchful.getConfig().combatConfig;
        HeatResistanceLevel level = HeatResistanceLevel.forMaterial(armorMaterial);
        return level.getHeatResistance(config);
    }

    public enum HeatResistanceLevel {
        VERY_PROTECTIVE(ArmorMaterialTags.VERY_RESISTANT_TO_HEAT, CombatConfig::getVeryProtectiveArmorHeatResistance),
        PROTECTIVE(ArmorMaterialTags.RESISTANT_TO_HEAT, CombatConfig::getProtectiveArmorHeatResistance),
        VERY_HARMFUL(ArmorMaterialTags.VERY_WEAK_TO_HEAT, CombatConfig::getVeryHarmfulArmorHeatResistance),
        NEUTRAL(SArmorMaterialTags.HEAT_NEUTRAL, c -> Double.NaN),
        DEFAULT(item -> true, CombatConfig::getDefaultArmorHeatResistance);


        private final Predicate<RegistryEntry<ArmorMaterial>> predicate;

        private final ToDoubleFunction<CombatConfig> heatResistanceProvider;

        HeatResistanceLevel(TagKey<ArmorMaterial> tag, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this(material -> material.isIn(tag), heatResistanceProvider);
        }

        HeatResistanceLevel(Predicate<RegistryEntry<ArmorMaterial>> predicate, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this.predicate = predicate;
            this.heatResistanceProvider = heatResistanceProvider;
        }

        public double getHeatResistance(CombatConfig config) {
            return this.heatResistanceProvider.applyAsDouble(config);
        }

        public boolean appliesToMaterial(RegistryEntry<ArmorMaterial> material) {
            return this.predicate.test(material);
        }

        public static HeatResistanceLevel forMaterial(RegistryEntry<ArmorMaterial> material) {
            for (HeatResistanceLevel level : values()) {
                if (level.appliesToMaterial(material)) {
                    return level;
                }
            }
            return DEFAULT;
        }
    }
}
