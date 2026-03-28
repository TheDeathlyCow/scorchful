package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.api.CollectWaterCallback;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.mixin.accessor.CauldronInteractionDispatcherAccessor;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class WaterSkinItem extends Item {
    public static final Style TOOLTIP_STYLE = DrinkContainer.TOOLTIP_STYLE;

    public static final Style PARCHING_STYLE = Style.EMPTY
            .withColor(ChatFormatting.RED);

    public static final Component FULL_ITEM_NAME = Component.translatable("item.scorchful.water_skin.filled");
    public static final Component PARTIALLY_FULL_ITEM_NAME = Component.translatable("item.scorchful.water_skin.partially_filled");
    public static final Component EMPTY_ITEM_NAME = Component.translatable("item.scorchful.water_skin.empty");

    public WaterSkinItem(Properties settings) {
        super(settings);
        ((CauldronInteractionDispatcherAccessor) CauldronInteractions.WATER).scorchful$put(this, this::onCauldronInteract);
    }

    @Override
    public ItemStack getDefaultInstance() {
        var itemStack = super.getDefaultInstance();
        itemStack.set(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT);
        return itemStack;
    }

    public static DrinkContainer getContainer(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT);
    }

    public static boolean hasDrink(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT).hasDrink();
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        InteractionResult refillResult = this.tryRefill(world, user, stack);
        if (refillResult != null) {
            return refillResult;
        }

        return super.use(world, user, hand);
    }

    @Override
    public Component getName(ItemStack stack) {
        DrinkContainer container = getContainer(stack);

        if (container.isEmpty()) {
            return EMPTY_ITEM_NAME;
        } else if (container.isFull()) {
            return FULL_ITEM_NAME;
        } else {
            return PARTIALLY_FULL_ITEM_NAME;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !getContainer(stack).isFull();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        DrinkContainer container = getContainer(stack);

        if (container.isEmpty()) {
            return 0;
        }

        return Math.round(container.getCurrentFill() * 13.0f);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fill = Math.max(0.0f, getContainer(stack).getCurrentFill());

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
        DrinkContainer.addDrinks(stack, amount);

        CollectWaterCallback.EVENT.invoker().onWaterCollected(player, stack, sourcePos);
    }

    @Nullable
    private InteractionResult tryRefill(Level world, Player user, ItemStack stack) {
        BlockHitResult blockHitResult = Item.getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            DrinkContainer container = stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT);

            if (!world.mayInteract(user, hitPos) || container.isFull()) {
                return null;
            }

            if (world.getFluidState(hitPos).is(FluidTags.WATER)) {
                if (!world.isClientSide()) {
                    this.fill(stack, user, world, hitPos, 4);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }

    private InteractionResult onCauldronInteract(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {

        if (!world.isClientSide()) {
            this.fill(stack, player, world, pos, 1);
            player.awardStat(Stats.USE_CAULDRON);
            LayeredCauldronBlock.lowerFillLevel(state, world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult onWarpedLilyInteract(
            BlockState state,
            Level world,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {
        if (getContainer(stack).isFull()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (state.getValue(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
            return InteractionResult.FAIL;
        }

        if (!world.isClientSide()) {
            this.fill(stack, player, world, pos, 4);
            player.awardStat(SStats.USE_WARPED_LILY);
            NetherLilyBlock.setWater(state, world, pos, 0);
        }
        return InteractionResult.SUCCESS;
    }
}
