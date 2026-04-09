package com.github.thedeathlycow.scorchful.datagen;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.datagen.generator.*;
import com.github.thedeathlycow.scorchful.datagen.generator.client.SModelGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.loot.ScorchfulBlockLootGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.registry.DamageTypeBootstrap;
import com.github.thedeathlycow.scorchful.datagen.generator.registry.TemperatureStatusGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.tag.*;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
        pack.addProvider(BootstrappedRegistryGenerator::new);
        pack.addProvider(TemperatureStatusTagGenerator::new);
        pack.addProvider(EnvironmentProviderTagGenerator::new);
        pack.addProvider(ClimateBiomeTagGenerator::new);
        pack.addProvider(EntityTypeTagGenerator::new);
        pack.addProvider(DamageTypeTagGenerator::new);

        BlockTagGenerator blockTags = pack.addProvider(BlockTagGenerator::new);
        pack.addProvider((output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTags));

        pack.addProvider(ScorchfulRecipeGenerator::new);
        pack.addProvider(ScorchfulBlockLootGenerator::new);
        pack.addProvider(SModelGenerator::new);
        pack.addProvider(EnglishUSGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        // this is needed to prevent crashes when looking up sources in generators
        registryBuilder.add(
                ThermooRegistries.TEMPERATURE_SOURCE,
                context -> {
                    context.register(
                            TemperatureSources.PASSIVE,
                            TemperatureSource.builder(Component.empty()).build()
                    );
                }
        );

        // actual generators

        registryBuilder.add(
                Registries.DAMAGE_TYPE,
                DamageTypeBootstrap::bootstrap
        );

        registryBuilder.add(
                ThermooRegistries.TEMPERATURE_STATUS,
                TemperatureStatusGenerator::bootstrap
        );
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Scorchful.MODID;
    }

    public static Identifier commonID(String path) {
        return Identifier.fromNamespaceAndPath("c", path);
    }
}