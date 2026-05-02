package com.github.thedeathlycow.scorchful.attachment;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Objects;

public class PlayerWaterAttachment implements INBTSerializable<CompoundTag> {

    public static final int MAX_WATER = 300;

    private static final String WATER_KEY = "body_water";

    private static final String REHYDRATION_DRINK_KEY = "rehydration_drink";


    private final Player provider;

    private int waterDrunk = 0;

    public PlayerWaterAttachment(IAttachmentHolder holder) {
        this.provider = holder instanceof Player player ? player : null;

        Objects.requireNonNull(this.provider, "Player water attachment created on non-player, this will cause a crash later.");
    }

    public int getWaterDrunk() {
        return waterDrunk;
    }

    public void drink(int amount) {
        this.waterDrunk = Mth.clamp(this.waterDrunk + amount, 0, MAX_WATER);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();

        if (tag.contains(WATER_KEY, Tag.TAG_INT)) {
            this.waterDrunk = tag.getInt(WATER_KEY);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (this.waterDrunk > 0) {
            tag.putInt(WATER_KEY, this.waterDrunk);
        }
    }

    public void serverTick() {
        if (!this.provider.thermoo$isCold() && ServerThirstPlugin.getActivePlugin().dehydrateFromSweating(this.provider)) {
            // sweating: move thirst water to wetness
            this.provider.thermoo$addWetTicks(2);
        }
    }
}
