package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public final class AccessoriesIntegration {
    public static boolean isEquipped(LivingEntity entity, Predicate<ItemStack> isEquipped) {
        if (ScorchfulIntegrations.isAccessoriesLoaded()) {
            AccessoriesCapability capability = AccessoriesCapability.get(entity);

            if (capability != null && capability.isEquipped(stack -> stack.is(SItemTags.IS_SUN_PROTECTING_HAT))) {
                return true;
            }
        }

        return false;
    }

    public static void removeAccessoriesRenderer() {
        if (ScorchfulIntegrations.isAccessoriesLoaded()) {
            AccessoriesRendererRegistry.registerNoRenderer(SItems.SUN_HAT);
        }
    }

    private AccessoriesIntegration() {

    }
}