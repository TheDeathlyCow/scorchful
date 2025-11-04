package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.Translate;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ScorchfulModMenu implements ModMenuApi {
    public static final String TITLE = "scorchful.title";

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
                .title(Text.literal("Immersive Storms Test"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Text.translatable(TITLE))
                                .option(createSubsectionButton(AccessibilitySettings.HANDLER, ACCESSIBILITY_CATEGORY, ACCESSIBILITY_DESC))
                                .option(createSubsectionButton(DisplaySettings.HANDLER, DISPLAY_CATEGORY, DISPLAY_DESC))
                                .option(createSubsectionButton(TemperatureConfig.HANDLER, TEMPERATURE_CATEGORY, TEMPERATURE_DESC))
                                .option(createSubsectionButton(EntityConfig.HANDLER, ENTITY_CATEGORY, ENTITY_DESC))
                                .option(createSubsectionButton(ItemConfig.HANDLER, ITEM_CATEGORY, ITEM_DESC))
                                .option(createSubsectionButton(WeatherConfig.HANDLER, WEATHER_CATEGORY, WEATHER_DESC))
                                .option(createSubsectionButton(DehydrationConfig.HANDLER, DEHYDRATION_CATEGORY, DEHYDRATION_DESC))
                                .build()
                )
                .build()
                .generateScreen(parent);
    }

    private static ButtonOption createSubsectionButton(ConfigClassHandler<?> handler, String titleKey, String descKey) {
        return ButtonOption.createBuilder()
                .name(Text.translatable(titleKey))
                .description(
                        OptionDescription.createBuilder()
                                .text(Text.translatable(descKey))
                                .build()
                )
                .text(Text.literal(""))
                .action((yaclScreen, buttonOption) -> {
                    MinecraftClient.getInstance()
                            .setScreen(handler
                                    .generateGui()
                                    .generateScreen(yaclScreen));
                }).build();
    }
}
