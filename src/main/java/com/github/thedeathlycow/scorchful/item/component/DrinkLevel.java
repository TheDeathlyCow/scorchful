package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ItemConfig;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.mixin.accessor.RegistryEntryReferenceAccessor;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public enum DrinkLevel implements StringRepresentable, ConsumableListener, TooltipProvider {
    PARCHING(
            "parching",
            SItemTags.IS_PARCHING,
            Component.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
            ItemConfig::getWaterFromParchingFood
    ),
    REFRESHING(
            "refreshing",
            SItemTags.IS_REFRESHING,
            Component.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ItemConfig::getWaterFromRefreshingFood
    ),
    SUSTAINING(
            "sustaining",
            SItemTags.IS_SUSTAINING,
            Component.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ItemConfig::getWaterFromSustainingFood
    ),
    HYDRATING(
            "hydrating",
            SItemTags.IS_HYDRATING,
            Component.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ItemConfig::getWaterFromHydratingFood
    );

    public static final Codec<DrinkLevel> CODEC = StringRepresentable.fromEnum(DrinkLevel::values);
    public static final IntFunction<DrinkLevel> ID_TO_VALUE = ByIdMap.continuous(
            DrinkLevel::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO
    );
    public static final StreamCodec<ByteBuf, DrinkLevel> PACKET_CODEC = ByteBufCodecs.idMapper(
            ID_TO_VALUE, DrinkLevel::ordinal
    );

    private final String name;

    private final TagKey<Item> tag;

    private final Component tooltipText;

    private final ToIntFunction<ItemConfig> waterProvider;

    DrinkLevel(String name, TagKey<Item> tag, Component tooltipText, ToIntFunction<ItemConfig> waterProvider) {
        this.name = name;
        this.tag = tag;
        this.tooltipText = tooltipText;
        this.waterProvider = waterProvider;
    }

    public static void applyToNewStack(ItemStack stack) {
        if (stack.has(SDataComponentTypes.DRINK_LEVEL)) {
            return;
        }

        if (((RegistryEntryReferenceAccessor) stack.getItem().builtInRegistryHolder()).scorchful$tags() == null) {
            return;
        }

        DrinkLevel level = byTag(stack);
        if (level != null) {
            stack.set(SDataComponentTypes.DRINK_LEVEL, level);
        }
    }

    public static void spawnWaterParticles(Level world, LivingEntity entity, int count) {
        RandomSource random = entity.getRandom();

        for (int i = 0; i < count; i++) {

            var velocity = new Vec3((random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 1, 0);
            velocity = velocity.xRot(-entity.getXRot() * (Mth.PI / 180f));
            velocity = velocity.yRot(-entity.getYRot() * (Mth.PI / 180f));

            double y = -random.nextFloat() * 0.6 - 0.3;
            var postion = new Vec3((random.nextFloat() - 0.5) * 0.3, y, 0.6);
            postion = postion.xRot(-entity.getXRot() * (Mth.PI / 180f));
            postion = postion.yRot(-entity.getYRot() * (Mth.PI / 180f));
            postion = postion.add(entity.getX(), entity.getEyeY(), entity.getZ());

            world.addParticle(
                    ParticleTypes.SPLASH,
                    postion.x, postion.y, postion.z,
                    velocity.x, velocity.y + 1, velocity.z
            );
        }
    }

    @Nullable
    private static DrinkLevel byTag(ItemStack stack) {
        for (DrinkLevel level : values()) {
            if (stack.is(level.tag)) {
                return level;
            }
        }

        return null;
    }

    public int getDrinkingWater(ItemConfig config) {
        return this.waterProvider.applyAsInt(config);
    }

    public Component getTooltipText() {
        return tooltipText;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public void onConsume(Level world, LivingEntity user, ItemStack stack, Consumable consumable) {
        if (ServerThirstPlugin.isCustomPluginLoaded()) {
            return;
        }

        PlayerWaterComponent waterComponent = ScorchfulComponents.PLAYER_WATER.getNullable(user);
        if (waterComponent != null) {
            int water = this.getDrinkingWater(ScorchfulConfig.getItemConfig());
            waterComponent.drink(water);

            if (waterComponent.getWaterDrunk() >= PlayerWaterComponent.MAX_WATER * 0.9) {
                user.playSound(SSoundEvents.ENTITY_GULP, 1f, 1f);
            }
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        tooltip.accept(this.tooltipText);
    }
}
