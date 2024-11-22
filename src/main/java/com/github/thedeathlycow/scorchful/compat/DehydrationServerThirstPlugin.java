package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class DehydrationServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public void onConsumeDrink(ServerPlayerEntity player, ItemStack stack) {
        // handled by dehydration mod
    }

    @Override
    public int tickSweatTransfer(PlayerEntity player) {
        return 0;
    }

    @Override
    public void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency) {
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();

        DehydrationConfig dehydrationConfig = Scorchful.getConfig().integrationConfig.dehydrationConfig;
        // dont drink if dont have to - prevents rehydration spam
        if (thirstManager.getThirstLevel() > dehydrationConfig.getMinWaterLevelForSweat()) {
            return;
        }

        int maxWater = MathHelper.floor(rehydrationEfficiency * dehydrationConfig.getMaxWaterLost());
        int waterToAdd = player.getRandom().nextBetween(1, maxWater);
        thirstManager.add(waterToAdd);
    }

    @Override
    public int getMaxRehydrationWaterCapacity() {
        return Scorchful.getConfig().getRehydrationDrinkSize(true);
    }
}