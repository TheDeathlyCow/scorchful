package com.github.thedeathlycow.scorchful.util;

import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class SWeighting {
    @Nullable
    public static <T, W extends Weighted> T getAt(List<T> pool, int totalWeight, Function<T, W> toWeighted) {
        for (T weighted : pool) {
            totalWeight -= toWeighted.apply(weighted).getWeight().getValue();
            if (totalWeight < 0) {
                return weighted;
            }
        }
        return null;
    }

    public static <T> int getWeightSum(List<T> pool, Function<T, ? extends Weighted> toWeighted) {
        long sum = 0L;

        for (T weighted : pool) {
            sum += toWeighted.apply(weighted).getWeight().getValue();
        }

        if (sum > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Sum of weights must be <= 2147483647");
        } else {
            return (int) sum;
        }
    }

    @Nullable
    private static <T, W extends Weighted> T getRandom(Random random, List<T> pool, int totalWeight, Function<T, W> toWeighted) {
        if (totalWeight < 0) {
            throw Util.getFatalOrPause(new IllegalArgumentException("Negative total weight in getRandomItem"));
        } else if (totalWeight == 0) {
            return null;
        } else {
            int i = random.nextInt(totalWeight);
            return getAt(pool, i, toWeighted);
        }
    }

    public static <T, W extends Weighted> T getRandom(Random random, List<T> pool, Function<T, W> toWeighted) {
        return getRandom(random, pool, getWeightSum(pool, toWeighted), toWeighted);
    }
}