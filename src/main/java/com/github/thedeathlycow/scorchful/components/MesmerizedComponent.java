package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.entity.effect.MesmerizedStatusEffect;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.List;

public class MesmerizedComponent implements Component, ServerTickingComponent {

    private final LivingEntity provider;

    private LivingEntity mesmerizedTarget;

    public MesmerizedComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public LivingEntity getMesmerizedTarget() {
        return mesmerizedTarget;
    }

    public void setMesmerizedTarget(LivingEntity mesmerizedTarget) {
        this.mesmerizedTarget = mesmerizedTarget;
    }

    public boolean hasMesmerizedTarget() {
        return this.mesmerizedTarget != null;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
    }

    @Override
    public void serverTick() {
        if (this.mesmerizedTarget != null) {
            boolean removeTarget = !this.provider.hasStatusEffect(SStatusEffects.MESMERIZED)
                    || !MesmerizedStatusEffect.IS_VALID_TARGET.test(this.mesmerizedTarget);
            if (removeTarget) {
                this.mesmerizedTarget = null;
            }
        } else {
            List<LivingEntity> nearby = this.provider.getWorld().getEntitiesByClass(
                    LivingEntity.class,
                    this.provider.getBoundingBox().expand(16),
                    MesmerizedStatusEffect.IS_VALID_TARGET
                            .and(entity -> entity != this.provider)
            );
            if (!nearby.isEmpty()) {
                this.setMesmerizedTarget(nearby.getFirst());
            }
        }
    }
}
