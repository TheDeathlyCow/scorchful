package com.github.thedeathlycow.scorchful.neoforge;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FabricDataGenHelper {
    private static final Logger LOGGER = Scorchful.LOGGER;

    /**
     * When enabled providers can enable extra validation, such as ensuring all registry entries have data generated for them.
     */
    private static final boolean STRICT_VALIDATION = System.getProperty("fabric-api.datagen.strict-validation") != null;

    private FabricDataGenHelper() {
    }

    public static void runDatagenForMod(String modid, String namespace, DataGeneratorEntrypoint entrypoint, GatherDataEvent event) {
        ModContainer modContainer =  FabricLoader.getInstance().getModContainer(modid).orElseThrow(() -> new RuntimeException("Failed to find effective mod container for mod id (%s)".formatted(modid)));

        Object2IntOpenHashMap<String> jsonKeySortOrders = (Object2IntOpenHashMap<String>) DataProvider.FIXED_ORDER_FIELDS;
        Object2IntOpenHashMap<String> defaultJsonKeySortOrders = new Object2IntOpenHashMap<>(jsonKeySortOrders);

        LOGGER.info("Running data generator for {}", modContainer.getMetadata().getId());

        try {
            HashSet<String> keys = new HashSet<>();
            entrypoint.addJsonKeySortOrders((key, value) -> {
                Objects.requireNonNull(key, "Tried to register a priority for a null key");
                jsonKeySortOrders.put(key, value);
                keys.add(key);
            });

            final RegistrySetBuilder builder = new RegistrySetBuilder();
            entrypoint.buildRegistry(builder);

            CompletableFuture<RegistrySetBuilder.PatchedRegistries> lookup = RegistryPatchGenerator.createLookup(event.getLookupProvider(), builder);

            if (!builder.getEntryKeys().isEmpty()) {
                event.getGenerator().addProvider(true, new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), lookup, Set.of(namespace)));
            }

            CompletableFuture<HolderLookup.Provider> registriesFuture = lookup.thenApply(RegistrySetBuilder.PatchedRegistries::full);
            FabricDataGenerator dataGenerator = new FabricDataGenerator(event.getGenerator(), event.getGenerator().getPackOutput().getOutputFolder(), modContainer, STRICT_VALIDATION, registriesFuture);
            entrypoint.onInitializeDataGenerator(dataGenerator);

            jsonKeySortOrders.keySet().removeAll(keys);
            jsonKeySortOrders.putAll(defaultJsonKeySortOrders);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to run data generator from mod (%s)".formatted(modContainer.getMetadata().getId()), t);
        }
    }

    /**
     * Used to keep track of conditions associated to generated objects.
     */
    private static final Map<Object, ResourceCondition[]> CONDITIONS_MAP = new IdentityHashMap<>();

    public static void addConditions(Object object, ResourceCondition[] conditions) {
        CONDITIONS_MAP.merge(object, conditions, ArrayUtils::addAll);
    }

    @Nullable
    public static ResourceCondition[] consumeConditions(Object object) {
        return CONDITIONS_MAP.remove(object);
    }

    /**
     * Adds {@code conditions} to {@code baseObject}.
     * @param baseObject the base JSON object to which the conditions are inserted
     * @param conditions the conditions to insert
     * @throws IllegalArgumentException if the object already has conditions
     */
    public static void addConditions(JsonObject baseObject, ResourceCondition... conditions) {
        if (baseObject.has(ResourceConditions.CONDITIONS_KEY)) {
            throw new IllegalArgumentException("Object already has a condition entry: " + baseObject);
        } else if (conditions == null || conditions.length == 0) {
            // Datagen might pass null conditions.
            return;
        }

        baseObject.add(ResourceConditions.CONDITIONS_KEY, ResourceCondition.LIST_CODEC.encodeStart(JsonOps.INSTANCE, Arrays.asList(conditions)).getOrThrow());
    }
}