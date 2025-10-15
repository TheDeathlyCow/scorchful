package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.mixin.accessor.RegistryEntryReferenceAccessor;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.Consumable;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public enum DrinkLevelComponent implements StringIdentifiable, Consumable, TooltipAppender {
    PARCHING(
            "parching",
            SItemTags.IS_PARCHING,
            Text.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
            ThirstConfig::getWaterFromParchingFood
    ),
    REFRESHING(
            "refreshing",
            SItemTags.IS_REFRESHING,
            Text.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromRefreshingFood
    ),
    SUSTAINING(
            "sustaining",
            SItemTags.IS_SUSTAINING,
            Text.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromSustainingFood
    ),
    HYDRATING(
            "hydrating",
            SItemTags.IS_HYDRATING,
            Text.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromHydratingFood
    );

    public static final Codec<DrinkLevelComponent> CODEC = StringIdentifiable.createCodec(DrinkLevelComponent::values);
    public static final IntFunction<DrinkLevelComponent> ID_TO_VALUE = ValueLists.createIndexToValueFunction(
            DrinkLevelComponent::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO
    );
    public static final PacketCodec<ByteBuf, DrinkLevelComponent> PACKET_CODEC = PacketCodecs.indexed(
            ID_TO_VALUE, DrinkLevelComponent::ordinal
    );

    private final String name;

    private final TagKey<Item> tag;

    private final Text tooltipText;

    private final ToIntFunction<ThirstConfig> waterProvider;

    DrinkLevelComponent(String name, TagKey<Item> tag, Text tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
        this.name = name;
        this.tag = tag;
        this.tooltipText = tooltipText;
        this.waterProvider = waterProvider;
    }

    public static void applyToNewStack(ItemStack stack) {
        if (stack.contains(SDataComponentTypes.DRINK_LEVEL)) {
            return;
        }

        if (((RegistryEntryReferenceAccessor) stack.getItem().getRegistryEntry()).scorchful$tags() == null) {
            return;
        }

        DrinkLevelComponent level = byTag(stack);
        if (level != null) {
            stack.set(SDataComponentTypes.DRINK_LEVEL, level);
        }
    }

    public static void spawnWaterParticles(World world, LivingEntity entity, int count) {
        Random random = entity.getRandom();

        for (int i = 0; i < count; i++) {

            var velocity = new Vec3d((random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 1, 0);
            velocity = velocity.rotateX(-entity.getPitch() * (MathHelper.PI / 180f));
            velocity = velocity.rotateY(-entity.getYaw() * (MathHelper.PI / 180f));

            double y = -random.nextFloat() * 0.6 - 0.3;
            var postion = new Vec3d((random.nextFloat() - 0.5) * 0.3, y, 0.6);
            postion = postion.rotateX(-entity.getPitch() * (MathHelper.PI / 180f));
            postion = postion.rotateY(-entity.getYaw() * (MathHelper.PI / 180f));
            postion = postion.add(entity.getX(), entity.getEyeY(), entity.getZ());

            world.addParticleClient(
                    ParticleTypes.SPLASH,
                    postion.x, postion.y, postion.z,
                    velocity.x, velocity.y + 1, velocity.z
            );
        }
    }

    @Nullable
    private static DrinkLevelComponent byTag(ItemStack stack) {
        for (DrinkLevelComponent level : values()) {
            if (stack.isIn(level.tag)) {
                return level;
            }
        }

        return null;
    }

    public int getDrinkingWater(ThirstConfig config) {
        return this.waterProvider.applyAsInt(config);
    }

    public Text getTooltipText() {
        return tooltipText;
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Override
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable) {
        if (ServerThirstPlugin.isCustomPluginLoaded()) {
            return;
        }

        PlayerWaterComponent waterComponent = ScorchfulComponents.PLAYER_WATER.getNullable(user);
        if (waterComponent != null) {
            int water = this.getDrinkingWater(ScorchfulConfig.getThirstConfig());
            waterComponent.drink(water);

            if (waterComponent.getWaterDrunk() >= PlayerWaterComponent.MAX_WATER * 0.9) {
                user.playSound(SSoundEvents.ENTITY_GULP, 1f, 1f);
            }
        }
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, ComponentsAccess components) {
        tooltip.accept(this.tooltipText);
    }
}
