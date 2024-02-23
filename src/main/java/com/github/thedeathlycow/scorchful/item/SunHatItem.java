package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SunHatItem extends Item implements Equipment {

    private static final Text TOOLTIP = Text.translatable(
            "item.scorchful.sun_hat.tooltip"
    ).setStyle(Style.EMPTY.withColor(TextColor.parse("blue")));

    public SunHatItem(Settings settings) {
        super(settings);
    }

    public static boolean isWearingSunHat(LivingEntity entity) {
        boolean isWearingInTrinketSlot = false;
        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.TRINKETS_ID)) {
            isWearingInTrinketSlot = TrinketsApi.getTrinketComponent(entity)
                    .map(trinketComponent -> trinketComponent.isEquipped(stack -> stack.isIn(SItemTags.IS_SUN_PROTECTING_HAT)))
                    .orElse(false);
        }
        return isWearingInTrinketSlot
                || entity.getEquippedStack(EquipmentSlot.HEAD).isIn(SItemTags.IS_SUN_PROTECTING_HAT);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(TOOLTIP);
        super.appendTooltip(stack, world, tooltip, context);
    }
}
