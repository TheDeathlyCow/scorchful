package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

public class SunHatItem extends Item implements Equipable {

    private static final Component TOOLTIP = Component.translatable(
            "item.scorchful.sun_hat.tooltip"
    ).setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));

    public SunHatItem(Properties settings) {
        super(settings);
    }

    public static ItemAttributeModifiers attributeModifiers() {
        return ItemAttributeModifiers.builder()
                .add(
                        ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                        new AttributeModifier(
                                Scorchful.id("sun_hat_resistance"),
                                0.25,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.HEAD
                )
                .build();
    }

    public static boolean isWearingSunHat(LivingEntity entity) {
        // TODO: accessories
//        boolean isWearingInTrinketSlot = false;
//        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.TRINKETS_ID)) {
//            isWearingInTrinketSlot = TrinketsApi.getTrinketComponent(entity)
//                    .map(trinketComponent -> trinketComponent.isEquipped(stack -> stack.is(SItemTags.IS_SUN_PROTECTING_HAT)))
//                    .orElse(false);
//        }
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(SItemTags.IS_SUN_PROTECTING_HAT);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, world, user, hand);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(TOOLTIP);
        super.appendHoverText(stack, context, tooltip, type);
    }
}
