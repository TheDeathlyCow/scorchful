package com.github.thedeathlycow.scorchful.datagen;

import com.github.thedeathlycow.scorchful.datagen.environment.EnvironmentProviderGenerator;
import com.github.thedeathlycow.scorchful.datagen.environment.EnvironmentProviderTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScorchfulDataGenerator implements DataGeneratorEntrypoint {

    public static final String MODID = "scorchful-datagen";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        LOGGER.info("Running Scorchful datagen");
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(EnvironmentProviderGenerator::new);
        pack.addProvider(EnvironmentProviderTagGenerator::new);
    }
}