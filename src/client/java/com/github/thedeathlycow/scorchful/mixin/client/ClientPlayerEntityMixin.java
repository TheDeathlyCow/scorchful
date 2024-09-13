package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
abstract class ClientPlayerEntityMixin extends PlayerEntity {

    @Shadow
    public Input input;

    @Shadow
    protected abstract boolean shouldAutoJump();

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @WrapOperation(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/input/Input;tick(ZF)V"
            )
    )
    private void moveForwardWhenMesmerized(
            Input instance,
            boolean slowDown,
            float slowDownFactor,
            Operation<Void> original
    ) {
        original.call(instance, slowDown, slowDownFactor);
        if (ScorchfulComponents.MESMERIZED.get(this).isMesmerized()) {
            instance.movementForward = MathHelper.clamp(slowDownFactor * 1.5f, -1f, 1f);
            instance.movementSideways = 0.0f;


        }
    }

    @Inject(
            method = "isAutoJumpEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private void autoJumpIfMesmerized(CallbackInfoReturnable<Boolean> cir) {
        if (ScorchfulComponents.MESMERIZED.get(this).isMesmerized()) {
            cir.setReturnValue(true);
        }
    }

}
