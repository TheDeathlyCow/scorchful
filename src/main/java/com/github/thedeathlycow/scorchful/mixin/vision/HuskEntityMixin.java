package com.github.thedeathlycow.scorchful.mixin.vision;

import com.github.thedeathlycow.scorchful.entity.DesertVisionChild;
import net.minecraft.entity.mob.HuskEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HuskEntity.class)
public class HuskEntityMixin implements DesertVisionChild {


    @Unique
    private boolean scorchful$isDesertVisisonChild = false;

    @Override
    @Unique
    public void scorchful$markAsDesertVisionChild() {
        scorchful$isDesertVisisonChild = true;
    }

    @Override
    @Unique
    public boolean scorchful$isDesertVisionChild() {
        return scorchful$isDesertVisisonChild;
    }
}
