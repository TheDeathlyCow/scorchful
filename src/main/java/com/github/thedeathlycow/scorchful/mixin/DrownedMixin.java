package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.DrownedEquipmentSelector;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public class DrownedMixin {
    @Inject(
            method = "populateDefaultEquipmentSlots",
            at = @At("RETURN")
    )
    private void populateDrownedArmor(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        DrownedEquipmentSelector.populateDrownedArmor((Drowned) (Object) this, random, difficulty);
    }
}