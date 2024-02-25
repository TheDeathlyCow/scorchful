package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class QuenchingFoods {

    public static void appendTooltip(ItemStack stack, List<Text> tooltip) {
        appendTooltip(stack, tooltip, QuenchingLevel.forItem(stack));
    }

    public static void appendTooltip(ItemStack stack, List<Text> tooltip, @Nullable QuenchingLevel level) {
        if (level != null && !ScorchfulIntegrations.isThirstModLoaded()) {
            tooltip.add(level.tooltipText);
        }
    }

    public static void onConsume(LivingEntity user, ItemStack stack) {
        onConsume(user, stack, QuenchingLevel.forItem(stack));
    }

    public static void onConsume(LivingEntity user, ItemStack stack, @Nullable QuenchingLevel level) {

        if (ScorchfulIntegrations.isThirstModLoaded()) {
            return;
        }

        if (level != null && user.isPlayer()) {
            ScorchfulComponents.PLAYER.get(user)
                    .drink(level.waterProvider.applyAsInt(Scorchful.getConfig().thirstConfig));
        }
    }

    public enum QuenchingLevel {
        REFRESHING(
                SItemTags.IS_REFRESHING,
                Text.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
                ThirstConfig::getWaterFromRefreshingFood
        ),
        SUSTAINING(
                SItemTags.IS_SUSTAINING,
                Text.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
                ThirstConfig::getWaterFromSustainingFood
        ),
        HYDRATING(
                SItemTags.IS_HYDRATING,
                Text.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
                ThirstConfig::getWaterFromHydratingFood
        );

        public final TagKey<Item> tag;

        public final Text tooltipText;

        public final ToIntFunction<ThirstConfig> waterProvider;

        QuenchingLevel(TagKey<Item> tag, Text tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
            this.tag = tag;
            this.tooltipText = tooltipText;
            this.waterProvider = waterProvider;
        }

        @Nullable
        public static QuenchingLevel forItem(ItemStack stack) {

            for (QuenchingLevel level : QuenchingLevel.values()) {
                if (stack.isIn(level.tag)) {
                    return level;
                }
            }

            return null;
        }
    }

    private QuenchingFoods() {

    }

}
