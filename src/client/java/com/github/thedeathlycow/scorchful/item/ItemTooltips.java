package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class ItemTooltips {

    private static final Text COOLING_TOOLTIP = Text.translatable("item.scorchful.tooltip.cooling")
            .setStyle(Style.EMPTY.withColor(Formatting.AQUA));

    public static void appendDrinkTooltip(ItemStack stack, Item.TooltipContext context, TooltipType tooltipType, List<Text> tooltip) {
        DrinkLevelComponent level = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (level != null && !ScorchfulIntegrations.isDehydrationLoaded()) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, level.getTooltipText());
            } else {
                tooltip.add(level.getTooltipText());
            }
        }
    }

    public static void appendCoolingTooltip(ItemStack stack, Item.TooltipContext context, TooltipType tooltipType, List<Text> tooltip) {
        if (stack.isIn(SItemTags.IS_COOLING_FOOD)) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, COOLING_TOOLTIP);
            } else {
                tooltip.add(COOLING_TOOLTIP);
            }
        }
    }

    private static void addTooltipBeforeAdvanced(ItemStack stack, List<Text> tooltip, Text tooltipText) {
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        Text idAsText = Text.literal(identifier.toString());

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            if (tooltip.get(i).contains(idAsText)) {
                tooltip.add(i, tooltipText);
                return;
            }
        }
    }

    private ItemTooltips() {

    }

}
