package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.components.PlayerComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.ToIntFunction;

public class QuenchingFoods {


    public static void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        QuenchingLevel level = QuenchingLevel.forItem(stack);
        if (level != null && !ScorchfulIntegrations.isDehydrationLoaded()) {
            if (context.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, level);
            } else {
                tooltip.add(level.tooltipText);
            }
        }
    }

    public static void onConsume(LivingEntity user, ItemStack stack) {
        onConsume(user, stack, QuenchingLevel.forItem(stack));
    }

    public static void onConsume(LivingEntity user, ItemStack stack, @Nullable QuenchingLevel level) {

        if (ScorchfulIntegrations.isDehydrationLoaded()) {
            return;
        }

        if (level != null && user instanceof PlayerEntity player) {
            PlayerComponent component = ScorchfulComponents.PLAYER.get(player);
            component.drink(level.waterProvider.applyAsInt(Scorchful.getConfig().thirstConfig));

            if (component.getWaterDrunk() >= PlayerComponent.MAX_WATER - 25) {
                player.playSound(SSoundEvents.ENTITY_GULP, player.getSoundCategory(), 1f, 1f);
            }

        }
    }

    public enum QuenchingLevel {
        PARCHING(
                SItemTags.IS_PARCHING,
                Text.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
                ThirstConfig::getWaterFromParchingFood
        ),
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

    private static void addTooltipBeforeAdvanced(ItemStack stack, List<Text> tooltip, QuenchingLevel level) {
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        Text idAsText = Text.literal(identifier.toString());

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            if (tooltip.get(i).contains(idAsText)) {
                tooltip.add(i, level.tooltipText);
                return;
            }
        }
    }

    private QuenchingFoods() {

    }

}
