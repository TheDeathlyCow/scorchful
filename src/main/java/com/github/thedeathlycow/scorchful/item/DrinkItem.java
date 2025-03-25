package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.io.Console;

public class DrinkItem extends Item {

    public static final int DRINK_TIME_TICKS = 32;
    private static final int START_DRINK_PARTICLES = DRINK_TIME_TICKS - 10;

    public DrinkItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        var itemStack = super.getDefaultStack();
        itemStack.set(SDataComponentTypes.DRINK_LEVEL, DrinkLevelComponent.HYDRATING);
        return itemStack;
    }
}
