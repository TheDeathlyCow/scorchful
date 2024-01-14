package com.github.thedeathlycow.scorchful.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class ScorchfulLivingEntityEvents {


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
