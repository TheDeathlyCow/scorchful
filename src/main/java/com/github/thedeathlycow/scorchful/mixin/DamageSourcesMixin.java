package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import com.github.thedeathlycow.scorchful.world.ScorchfulDamageSources;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements ScorchfulDamageSources {
    @Shadow
    public abstract DamageSource source(ResourceKey<DamageType> key);

    @Unique
    private DamageSource scorchfulSuffocate;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(RegistryAccess registries, CallbackInfo ci) {
        this.scorchfulSuffocate = this.source(SDamageTypes.SUFFOCATE);
    }

    @Override
    @Unique
    public DamageSource scorchfulSuffocate() {
        return this.scorchfulSuffocate;
    }
}