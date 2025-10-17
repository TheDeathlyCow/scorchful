package com.github.thedeathlycow.scorchful.datagen;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.datagen.generator.*;
import com.github.thedeathlycow.scorchful.datagen.generator.client.SModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScorchfulDataGenerator implements DataGeneratorEntrypoint {

    public static final String MODID = "scorchful-datagen";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        LOGGER.info("Running Scorchful datagen");
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(EnvironmentProviderTagGenerator::new);
        pack.addProvider(ClimateBiomeTagGenerator::new);

        BlockTagGenerator blockTags = pack.addProvider(BlockTagGenerator::new);
        pack.addProvider((output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTags));

        pack.addProvider(ScorchfulRecipeGenerator::new);

        pack.addProvider(SModelGenerator::new);

        pack.addProvider(EnglishUSGenerator::new);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Scorchful.MODID;
    }

    public static Identifier commonID(String path) {
        return Identifier.of("c", path);
    }
}