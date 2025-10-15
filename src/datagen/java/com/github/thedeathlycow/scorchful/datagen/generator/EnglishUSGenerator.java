package com.github.thedeathlycow.scorchful.datagen.generator;


import com.github.thedeathlycow.scorchful.ScorchfulModMenu;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class EnglishUSGenerator extends FabricLanguageProvider {
    public EnglishUSGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
        builder.add(ScorchfulModMenu.TITLE, "Immersive Storms Config");
        builder.add(ScorchfulModMenu.CLIENT_CATEGORY, "Client Settings");
        builder.add(ScorchfulModMenu.CLIENT_CATEGORY_DESC, "Display settings for Scorchful");
        builder.add(ScorchfulModMenu.COMBAT_CATEGORY, "Combat Settings");
        builder.add(ScorchfulModMenu.COMBAT_CATEGORY_DESC, "Specific settings for combat");

        generateConfigOptionTranslations(ClientConfig.HANDLER, builder);
        generateConfigOptionTranslations(CombatConfig.HANDLER, builder);
    }

    private <T> void generateConfigOptionTranslations(
            ConfigClassHandler<T> handler,
            TranslationBuilder builder
    ) {
        final String prefix = Translate.prefixKey(handler);

        for (Field field : handler.configClass().getDeclaredFields()) {
            SerialEntry entry = field.getAnnotation(SerialEntry.class);
            if (entry == null) {
                continue;
            }

            Translate.Name nameData = field.getAnnotation(Translate.Name.class);
            String nameKey = configOption(prefix, field.getName());

            if (nameData != null) {
                builder.add(nameKey, nameData.value());
            } else {
                throw new IllegalStateException("Option name missing for" + nameKey);
            }

            String comment = entry.comment();
            String commentKey = commentKey(prefix, field.getName());

            if (comment != null && !comment.isEmpty()) {
                builder.add(commentKey, comment);
            } else if (field.getAnnotation(Translate.NoComment.class) == null) {
                throw new IllegalStateException("Missing comment or @NoComment marker for " + commentKey);
            }
        }
    }

    private static String configOption(String prefix, String name) {
        return prefix + "." + name;
    }

    private static String commentKey(String prefix, String name) {
        return configOption(prefix, name) + ".desc";
    }
}