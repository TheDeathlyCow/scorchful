package com.github.thedeathlycow.scorchful.mixin.client.entity.render;

import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements SLivingEntityRenderState {
    @Unique
    private boolean scorchful$hasSunHat = false;

    @Override
    @Unique
    public boolean scorchful$hasSunHat() {
        return scorchful$hasSunHat;
    }

    @Override
    @Unique
    public void scorchful$hasSunHat(boolean value) {
        scorchful$hasSunHat = value;
    }
}