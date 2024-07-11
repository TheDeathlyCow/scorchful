package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
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
        DrinkLevelComponent level = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (level != null && !ScorchfulIntegrations.isDehydrationLoaded()) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, level);
            } else {
                tooltip.add(level.getTooltipText());
            }
        }
    }

    private static void addTooltipBeforeAdvanced(ItemStack stack, List<Text> tooltip, DrinkLevelComponent level) {
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        Text idAsText = Text.literal(identifier.toString());

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            if (tooltip.get(i).contains(idAsText)) {
                tooltip.add(i, level.getTooltipText());
                return;
            }
        }
    }

    private DrinkItemHelper() {

    }

}
