package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.Translate;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ScorchfulModMenu implements ModMenuApi {
    public static final String TITLE = "scorchful.title";
    public static final String CLIENT_TITLE = "scorchful.config.client.title";
    public static final String COMMON_TITLE = "scorchful.config.common.title";
    public static final String COMPAT_TITLE = "scorchful.config.compat.title";

    public static final String ACCESSIBILITY_CATEGORY = Translate.mainCategoryKey(AccessibilitySettings.HANDLER);
    public static final String DISPLAY_CATEGORY = Translate.mainCategoryKey(DisplaySettings.HANDLER);
    public static final String TEMPERATURE_CATEGORY = Translate.mainCategoryKey(TemperatureConfig.HANDLER);
    public static final String ENTITY_CATEGORY = Translate.mainCategoryKey(EntityConfig.HANDLER);
    public static final String ITEM_CATEGORY = Translate.mainCategoryKey(ItemConfig.HANDLER);
    public static final String WEATHER_CATEGORY = Translate.mainCategoryKey(WeatherConfig.HANDLER);
    public static final String DEHYDRATION_CATEGORY = Translate.mainCategoryKey(DehydrationConfig.HANDLER);

    public static final String ACCESSIBILITY_DESC = Translate.descKey(AccessibilitySettings.HANDLER);
    public static final String DISPLAY_DESC = Translate.descKey(DisplaySettings.HANDLER);
    public static final String TEMPERATURE_DESC = Translate.descKey(TemperatureConfig.HANDLER);
    public static final String ENTITY_DESC = Translate.descKey(EntityConfig.HANDLER);
    public static final String ITEM_DESC = Translate.descKey(ItemConfig.HANDLER);
    public static final String WEATHER_DESC = Translate.descKey(WeatherConfig.HANDLER);
    public static final String DEHYDRATION_DESC = Translate.descKey(DehydrationConfig.HANDLER);


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Immersive Storms Test"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Component.translatable(TITLE))
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.translatable(CLIENT_TITLE))
                                                .option(createSubsectionButton(AccessibilitySettings.HANDLER, ACCESSIBILITY_CATEGORY, ACCESSIBILITY_DESC))
                                                .option(createSubsectionButton(DisplaySettings.HANDLER, DISPLAY_CATEGORY, DISPLAY_DESC))
                                                .build()
                                )
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.translatable(COMMON_TITLE))
                                                .option(createSubsectionButton(TemperatureConfig.HANDLER, TEMPERATURE_CATEGORY, TEMPERATURE_DESC))
                                                .option(createSubsectionButton(EntityConfig.HANDLER, ENTITY_CATEGORY, ENTITY_DESC))
                                                .option(createSubsectionButton(ItemConfig.HANDLER, ITEM_CATEGORY, ITEM_DESC))
                                                .option(createSubsectionButton(WeatherConfig.HANDLER, WEATHER_CATEGORY, WEATHER_DESC))
                                                .build()
                                )
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.translatable(COMPAT_TITLE))
                                                .option(createSubsectionButton(DehydrationConfig.HANDLER, DEHYDRATION_CATEGORY, DEHYDRATION_DESC))
                                                .build()
                                )
                                .build()
                )
                .build()
                .generateScreen(parent);
    }

    private static ButtonOption createSubsectionButton(ConfigClassHandler<?> handler, String titleKey, String descKey) {
        return ButtonOption.createBuilder()
                .name(Component.translatable(titleKey))
                .description(
                        OptionDescription.createBuilder()
                                .text(Component.translatable(descKey))
                                .build()
                )
                .text(Component.literal(""))
                .action((yaclScreen, buttonOption) -> {
                    Minecraft.getInstance()
                            .setScreen(handler
                                    .generateGui()
                                    .generateScreen(yaclScreen));
                }).build();
    }
}
