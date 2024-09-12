package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.entity.effect.MesmerizedStatusEffect;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class MesmerizedComponent implements Component, ServerTickingComponent {

    private final LivingEntity provider;

    private Entity mesmerizedTarget;

    public MesmerizedComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public Entity getMesmerizedTarget() {
        return mesmerizedTarget;
    }

    public void setMesmerizedTarget(Entity mesmerizedTarget) {
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
        }
    }
}
