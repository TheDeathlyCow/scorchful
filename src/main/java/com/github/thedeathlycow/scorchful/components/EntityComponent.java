package com.github.thedeathlycow.scorchful.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class EntityComponent implements Component {

    private final Entity provider;

    private boolean isDesertVisionChild = false;

    public EntityComponent(Entity provider) {
        this.provider = provider;
    }

    public boolean isDesertVisionChild() {
        return isDesertVisionChild;
    }

    public void makeDesertVisionChild() {
        isDesertVisionChild = true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        // children should be transient, so not saved to NBT
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // children should be transient, so not saved to NBT
    }
}
