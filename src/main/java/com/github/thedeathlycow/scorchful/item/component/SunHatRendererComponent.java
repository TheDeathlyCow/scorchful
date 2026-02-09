package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record SunHatRendererComponent(
        boolean replaceArmorModel,
        boolean showTooltip
) implements TooltipProvider {
    public static final SunHatRendererComponent DEFAULT = new SunHatRendererComponent(true, true);

    public static final Codec<SunHatRendererComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .lenientOptionalFieldOf("replace_armor_model", DEFAULT.replaceArmorModel)
                            .forGetter(SunHatRendererComponent::replaceArmorModel),
                    Codec.BOOL
                            .lenientOptionalFieldOf("show_tooltip", DEFAULT.showTooltip)
                            .forGetter(SunHatRendererComponent::showTooltip)
            ).apply(instance, SunHatRendererComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SunHatRendererComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SunHatRendererComponent::replaceArmorModel,
            ByteBufCodecs.BOOL,
            SunHatRendererComponent::showTooltip,
            SunHatRendererComponent::new
    );

    public static final Identifier SHADE_OVERLAY_TEXTURE = Scorchful.id("misc/shade_overlay");

    private static final Component TOOLTIP = Component.translatable(
            "item.scorchful.sun_hat.tooltip"
    ).setStyle(
            Style.EMPTY
                    .withColor(ChatFormatting.BLUE)
                    .withItalic(true)
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        if (this.showTooltip()) {
            tooltip.accept(TOOLTIP);
        }
    }
}