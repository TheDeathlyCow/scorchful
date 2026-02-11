package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

import java.util.function.Consumer;

public record SunHatRenderer(
        boolean replaceArmorModel,
        boolean showTooltip
) implements TooltipProvider {
    public static final SunHatRenderer DEFAULT = new SunHatRenderer(true, true);

    public static final Codec<SunHatRenderer> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .lenientOptionalFieldOf("replace_armor_model", DEFAULT.replaceArmorModel)
                            .forGetter(SunHatRenderer::replaceArmorModel),
                    Codec.BOOL
                            .lenientOptionalFieldOf("show_tooltip", DEFAULT.showTooltip)
                            .forGetter(SunHatRenderer::showTooltip)
            ).apply(instance, SunHatRenderer::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SunHatRenderer> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SunHatRenderer::replaceArmorModel,
            ByteBufCodecs.BOOL,
            SunHatRenderer::showTooltip,
            SunHatRenderer::new
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