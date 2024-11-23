package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public abstract class DrinkItem extends Item {

    public static final int DRINK_TIME_TICKS = 32;
    private static final int START_DRINK_PARTICLES = DRINK_TIME_TICKS - 10;

    public DrinkItem(Settings settings) {
        super(settings);
    }

    protected abstract ItemStack getPostConsumeStack(ItemStack stack, World world, ServerPlayerEntity serverPlayer);

    @Override
    public ItemStack getDefaultStack() {
        var itemStack = super.getDefaultStack();
        itemStack.set(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING);
        return itemStack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (world.isClient && remainingUseTicks < START_DRINK_PARTICLES) {
            spawnWaterParticles(world, user, 2);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.emitGameEvent(GameEvent.DRINK);

        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            return this.getPostConsumeStack(super.finishUsing(stack, world, user), world, serverPlayer);
        } else {
            return super.finishUsing(stack, world, user);
        }
    }

    /**
     * Called from {@link com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents#CONSUME_ITEM}. Applies water from drinking to the user.
     *
     * @param stack  Stack being consumed
     * @param player Player consuming the drink
     */
    public static void applyWater(ItemStack stack, ServerPlayerEntity player) {
        if (ServerThirstPlugin.isCustomPluginLoaded()) {
            return;
        }

        DrinkLevelComponent drink = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (drink == null) {
            return;
        }

        PlayerWaterComponent component = ScorchfulComponents.PLAYER_WATER.get(player);

        int water = drink.getDrinkingWater(Scorchful.getConfig().thirstConfig);
        component.drink(water);

        if (component.getWaterDrunk() >= PlayerWaterComponent.MAX_WATER * 0.9) {
            player.playSound(SSoundEvents.ENTITY_GULP, 1f, 1f);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return DRINK_TIME_TICKS;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    private static void spawnWaterParticles(World world, LivingEntity entity, int count) {
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

            world.addParticle(
                    ParticleTypes.SPLASH,
                    postion.x, postion.y, postion.z,
                    velocity.x, velocity.y + 1, velocity.z
            );
        }
    }
}
