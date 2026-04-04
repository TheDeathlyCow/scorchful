package com.github.thedeathlycow.scorchful.client.entity.state;

public interface SLivingEntityRenderState {
    default boolean scorchful$hasSunHat() {
        throw new AssertionError("Implemented in mixin");
    }

    default void scorchful$hasSunHat(boolean value) {
        throw new AssertionError("Implemented in mixin");
    }
}
