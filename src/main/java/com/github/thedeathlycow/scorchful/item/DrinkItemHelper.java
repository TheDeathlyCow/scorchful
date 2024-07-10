package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class DrinkItemHelper {


    public static void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipType tooltipType, List<Text> tooltip) {
        Level level = Level.forItem(stack);
        if (level != null && !ScorchfulIntegrations.isDehydrationLoaded()) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, level);
            } else {
                tooltip.add(level.tooltipText);
            }
        }
    }

    public static void modifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context) {
        for (Level level: Level.values()) {
            context.modify(
                    item -> item.getRegistryEntry().isIn(level.tag),
                    (builder, item) -> {
                        ThirstConfig config = Scorchful.getConfig().thirstConfig;
                        builder.add(SDataComponentTypes.DRINKING_WATER, level.getDrinkingWater(config));
                    }
            );
        }
    }

    public enum Level {
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

        Level(TagKey<Item> tag, Text tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
            this.tag = tag;
            this.tooltipText = tooltipText;
            this.waterProvider = waterProvider;
        }

        @Nullable
        public static DrinkItemHelper.Level forItem(ItemStack stack) {

            for (Level level : Level.values()) {
                if (stack.isIn(level.tag)) {
                    return level;
                }
            }

            return null;
        }

        public int getDrinkingWater(ThirstConfig config) {
            return this.waterProvider.applyAsInt(config);
        }
    }

    private static void addTooltipBeforeAdvanced(ItemStack stack, List<Text> tooltip, Level level) {
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        Text idAsText = Text.literal(identifier.toString());

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            if (tooltip.get(i).contains(idAsText)) {
                tooltip.add(i, level.tooltipText);
                return;
            }
        }
    }

    private DrinkItemHelper() {

    }

}
