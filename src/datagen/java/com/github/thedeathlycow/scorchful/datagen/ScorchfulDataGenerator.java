package com.github.thedeathlycow.scorchful.datagen;

import com.github.thedeathlycow.scorchful.datagen.generator.BlockTagGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.ClimateBiomeTagGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.EnvironmentProviderTagGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.ItemTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.ResourceLocation;
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
    }

    public static ResourceLocation commonID(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}