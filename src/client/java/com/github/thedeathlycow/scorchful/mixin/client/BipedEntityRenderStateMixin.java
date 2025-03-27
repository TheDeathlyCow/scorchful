package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.entity.state.SBipedEntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BipedEntityRenderState.class)
public class BipedEntityRenderStateMixin implements SBipedEntityRenderState {
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