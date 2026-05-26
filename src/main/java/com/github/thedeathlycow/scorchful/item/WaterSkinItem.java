package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.api.CollectWaterCallback;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.SStats;
import dev.ghen.thirst.content.purity.WaterPurity;
import dev.ghen.thirst.content.registry.ThirstComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterSkinItem extends DrinkItem {

    public static final Style TOOLTIP_STYLE = Style.EMPTY
            .withColor(ChatFormatting.AQUA);

    public static final Style PARCHING_STYLE = Style.EMPTY
            .withColor(ChatFormatting.RED);

    public static final int MAX_DRINKS = 16;

    public WaterSkinItem(Properties settings) {
        super(settings);
        CauldronInteraction.WATER.map().put(this, this::onCauldronInteract);
    }

    @Override
    protected ItemStack getPostConsumeStack(ItemStack stack, Level world, ServerPlayer serverPlayer) {
        if (!serverPlayer.isCreative()) {
            addDrinks(stack, -1);
        }

        return stack;
    }

    @Override
    public ItemStack getDefaultInstance() {
        var itemStack = super.getDefaultInstance();
        itemStack.set(SDataComponentTypes.NUM_DRINKS, 0);
        return itemStack;
    }

    public static int getNumDrinks(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.NUM_DRINKS, 0);
    }

    public static float getFill(ItemStack stack) {
        return (float) getNumDrinks(stack) / MAX_DRINKS;
    }

    public static boolean hasDrink(ItemStack stack) {
        return getNumDrinks(stack) > 0;
    }

    public static void addDrinks(ItemStack stack, int value) {
        stack.update(
                SDataComponentTypes.NUM_DRINKS,
                0,
                currentDrinks -> Mth.clamp(currentDrinks + value, 0, MAX_DRINKS)
        );
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);

        return switch (numDrinks) {
            case 0 -> "item.scorchful.water_skin.empty";
            case MAX_DRINKS -> "item.scorchful.water_skin.filled";
            default -> "item.scorchful.water_skin.partially_filled";
        };
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipType) {
        int numDrinks = getNumDrinks(stack);

        if (numDrinks > 0) {
            super.appendHoverText(stack, context, tooltip, tooltipType);
        }

        MutableComponent text = numDrinks > 0
                ? Component.translatable("item.scorchful.water_skin.tooltip.count", numDrinks, MAX_DRINKS)
                : Component.translatable("item.scorchful.water_skin.tooltip.empty");
        text.setStyle(TOOLTIP_STYLE);

        tooltip.add(text);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        InteractionResultHolder<ItemStack> refillResult = this.tryRefill(world, user, stack);
        if (refillResult != null) {
            return refillResult;
        }

        if (hasDrink(stack)) {
            return super.use(world, user, hand);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!hasDrink(stack)) {
            return stack;
        }

        return super.finishUsingItem(stack, world, user);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return hasDrink(stack) ? DrinkItem.DRINK_TIME_TICKS : 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return hasDrink(stack) ? UseAnim.DRINK : UseAnim.NONE;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);
        return numDrinks < MAX_DRINKS;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);
        if (numDrinks == 0) {
            return 0;
        }

        return Math.round(((float) numDrinks / MAX_DRINKS) * 13.0f);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fill = Math.max(0.0f, getFill(stack));

        float saturationValue = Mth.clampedMap(fill, 0f, 1f, 0.5f, 1.0f);

        return Mth.hsvToRgb(210f / 360f, saturationValue, saturationValue);
    }

    protected void fill(ItemStack stack, Player player, Level world, BlockPos sourcePos, int amount) {
        world.playSound(
                null,
                player.blockPosition(),
                SSoundEvents.ITEM_WATER_SKIN_FILL, SoundSource.PLAYERS,
                1.0f, 1.0f
        );
        world.gameEvent(player, GameEvent.FLUID_PICKUP, sourcePos);
        player.awardStat(Stats.ITEM_USED.get(this));
        addDrinks(stack, amount);

        CollectWaterCallback.EVENT.invoker().onWaterCollected(player, stack, sourcePos);

        if (ScorchfulIntegrations.isThirstWasTakenLoaded()) {
            int collectionPurity = WaterPurity.getBlockPurity(world, sourcePos);
            int currentPurity = stack.getOrDefault(ThirstComponent.PURITY, 0);

            if (collectionPurity <= currentPurity) {
                stack.set(ThirstComponent.PURITY, collectionPurity);
            }
        }
    }

    @Nullable
    private InteractionResultHolder<ItemStack> tryRefill(Level world, Player user, ItemStack stack) {
        BlockHitResult blockHitResult = Item.getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            if (!world.mayInteract(user, hitPos) || getNumDrinks(stack) >= MAX_DRINKS) {
                return null;
            }

            if (world.getFluidState(hitPos).is(FluidTags.WATER)) {
                if (!world.isClientSide) {
                    this.fill(stack, user, world, hitPos, 4);
                }
                return InteractionResultHolder.sidedSuccess(
                        stack,
                        world.isClientSide()
                );
            }
        }
        return null;
    }

    private ItemInteractionResult onCauldronInteract(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {

        if (!world.isClientSide) {
            this.fill(stack, player, world, pos, 1);
            player.awardStat(Stats.USE_CAULDRON);
            LayeredCauldronBlock.lowerFillLevel(state, world, pos);
        }
        return ItemInteractionResult.sidedSuccess(world.isClientSide);
    }

    public ItemInteractionResult onWarpedLilyInteract(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {
        if (getNumDrinks(stack) >= MAX_DRINKS) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (state.getValue(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
            return ItemInteractionResult.FAIL;
        }

        if (!world.isClientSide) {
            this.fill(stack, player, world, pos, 4);
            player.awardStat(SStats.USE_WARPED_LILY);
            NetherLilyBlock.setWater(state, world, pos, 0);
        }
        return ItemInteractionResult.sidedSuccess(world.isClientSide);
    }

}
