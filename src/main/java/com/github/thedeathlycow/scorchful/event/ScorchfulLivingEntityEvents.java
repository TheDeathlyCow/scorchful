package com.github.thedeathlycow.scorchful.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class ScorchfulLivingEntityEvents {


    /**
     * @deprecated use {@link net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents#AFTER_DAMAGE}
     */
    @Deprecated(since = "0.11.2", forRemoval = true)
    public static final Event<OnDamagedCallback> ON_DAMAGED = EventFactory.createArrayBacked(
            OnDamagedCallback.class,
            callbacks -> (entity, source, amount) -> {
                for (OnDamagedCallback callback : callbacks) {
                    callback.onDamaged(entity, source, amount);
                }
            }
    );

    @FunctionalInterface
    public interface OnDamagedCallback {

        void onDamaged(LivingEntity entity, DamageSource source, float amount);

    }

}
