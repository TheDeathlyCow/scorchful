package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.attachment.PlayerWaterAttachment;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public abstract class DrinkItem extends Item {

    public static final int DRINK_TIME_TICKS = 32;
    private static final int START_DRINK_PARTICLES = DRINK_TIME_TICKS - 10;

    public DrinkItem(Properties settings) {
        super(settings);
    }

    protected abstract ItemStack getPostConsumeStack(ItemStack stack, Level world, ServerPlayer serverPlayer);

    @Override
    public ItemStack getDefaultInstance() {
        var itemStack = super.getDefaultInstance();
        itemStack.set(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING);
        return itemStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);
        if (world.isClientSide && remainingUseTicks < START_DRINK_PARTICLES) {
            spawnWaterParticles(world, user, 2);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        user.gameEvent(GameEvent.DRINK);

        if (user instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return this.getPostConsumeStack(super.finishUsingItem(stack, world, user), world, serverPlayer);
        } else {
            return super.finishUsingItem(stack, world, user);
        }
    }

    /**
     * Called from {@link com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents#CONSUME_ITEM}. Applies water from drinking to the user.
     *
     * @param stack  Stack being consumed
     * @param player Player consuming the drink
     */
    public static void applyWater(ItemStack stack, ServerPlayer player) {
        if (ServerThirstPlugin.isCustomPluginLoaded()) {
            return;
        }

        DrinkLevelComponent drink = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (drink == null) {
            return;
        }

        PlayerWaterAttachment component = player.getData(ScorchfulEntityAttachments.PLAYER_WATER);

        int water = drink.getDrinkingWater(Scorchful.getConfig().thirstConfig);
        component.drink(water);

        if (component.getWaterDrunk() >= PlayerWaterAttachment.MAX_WATER * 0.9) {
            player.playSound(SSoundEvents.ENTITY_GULP, 1f, 1f);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return DRINK_TIME_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    private static void spawnWaterParticles(Level world, LivingEntity entity, int count) {
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
}
