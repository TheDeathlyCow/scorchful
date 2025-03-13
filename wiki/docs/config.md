---
title: ⚙️ Config File
---

The Scorchful Config file is a bit of a beast, but this guide will help walk you through what each of the options do. Items marked with :star: are the "important" options that are fairly straightforward and are the ones you most likely will be wanting to adjust.

!!! tip
    Items that are most relevant to mod pack authors have been marked with :star:

!!! warning
    This page reflects the latest version of the config for Minecraft 1.21.1. Items documented here may not necessarily reflect what appears in an older version of Scorchful. Starting from Scorchful 0.15, removed options will be documented in [Removed Config Options](#removed-config-options).

## Update Config

The update config is used to enable or disable automatic config updates when significant changes are made to the default config values.

* Current config version `currentConfigVersion`: This just saves the current version of the config, you should not touch this.
* :star: Enable config auto updates: Whether to allow the config to reset itself whenever the config is out of date. For most modpacks, I'd recommend turning this off.

## Client Config

Config options that relate to various client-sided effects. Note that while this section appears on dedicated servers, changing it there has no effect on players.

* Do burning heart overlay `doBurningHeartOverlay`: Enable/disable the burning heart overlay temperature indicator
* Do soaking overlay `doSoakingOverlay`: Enable/disable the soaking overlay on the health bar
* Do Sun Hat shading: `doSunHatShading`: Enable/disable the shading effect of wearing a Sun Hat
* :star: Enable sound temperature effects `enableSoundTemperatureEffects`: Used to enable/disable the heartbeat sound when warm.
* Enable drip particles when wet `enableWetDripParticles`: Enable/disable the drip particles when wet
* :star: Enable heat stroke post processing `enableHeatStrokePostProcessing`: Enable/disable the blur and wobble effects of Heat Stroke. May improve performance on low end PCs
* :star: Enable fear post processing `enableFearPostProcessing`: Enable/disable the desaturation effects of the Fear status effect
* Sun Hat shade opacity `sunHatShadeOpacity`: Controls how dark the sun hat shading is
* :star: Enable sandstorm particles `enableSandstormParticles`: Enable/disable dust particles during [Sandstorms](./Sandstorms)
* :star: Enable sandstorm fog `enableSandstormFog`: Enable/disable the dense fog effect during Sandstorms
* :star: Enable sandstorm sounds `enableSandstormSounds`: Enable/disable the wind sounds during Sandstorms
* Sandstorm particle render distance `sandStormParticleRenderDistance`: How far from the camera to render sandstorm dust particles
* Sandstorm particle rarity `sandStormParticleRarity`: How many sandstorm dust particles should appear, higher value = fewer particles
* Sandstorm particle Z axis velocity `sandStormParticleVelocity`: How fast the sandstorm dust particles move
* Sandstorm fog start distance `sandStormFogStart`: How far away to start rendering sandstorm fog
* Sandstorm fog end distance `sandStormFogEnd`: How far away to stop rendering sandstorm fog

## Heating Config

Server-side options for temperature and heating.

* :star: Do passive heating `doPassiveHeating`: Enable/disable passive environmental heating
* :star: Passive heating tick interval `passiveHeatingTickInterval`: How many ticks should occur between applying heat to a player from the environment. Setting this to large values can be used to slow heat down.
* :star: Max passive heating percent (0-1) `maxPassiveHeatingScale`: The maximum scale that the environment can heat heat players to. Given as a percentage from 0 to 1 (0 = 0%, 1 = 100%)
* Enable Turtle Armor status effects `enableTurtleArmorEffects`: Enable/disable water breathing from Turtle Armor
* :star: Min temperature for heat (in °C) `minTemperatureForHeatC`: Cutoff temperature for overheating in Celsius. Biomes at or above this temperature will apply environment heating to players. May not be less than 25°C.
* :star: Degrees per temperature level increase (in °C/°K) `degreesCPerTemperatureIncrease`: Specifies the number of Celsius/Kelvin degrees the temperature must be above the minimum heat threshold for each one-point increase in player temperature per tick. Must be positive and non-zero.
* Cooling from ice `coolingFromIce`: How much temperature to remove each tick that the player is standing on packed or blue ice
* :star: Scorching biome heat increase `scorchingBiomeHeatIncrease`: How much to increase the environmental heating by when out in the Sun in a Scorching biome (Deserts and Badlands)
* On fire warm rate `onFireWarmRate`: How much to increase the temperature of entities on fire each tick
* On fire warm rate with Fire Resistance `onFireWarmRateWithFireResistance`: How much to increase the temperature of entities on fire each tick, when they have Fire Resistance
* In lava warm rate `inLavaWarmRate`: The temperature change to apply to entities swimming in Lava each tick
* Strider out of lava cool rate `striderOutOfLavaCoolRate`: The cooling to apply only to Striders each tick they are not in Lava
* Powder Snow cool rate when warm `powderSnowCoolRate`: The cooling to apply to entities in Powder Snow each tick (only applies when their temperature is > 0)
* Fireball strike heat `fireballHeat`: The amount of heat to apply to entities when struck by a fire ball / fire charge
* Water breathing duration per worn piece of Turtle Armor (seconds) `waterBreathingDurationPerTurtleArmorPieceSeconds`: The amount of time to add to the dive water breathing per piece of turtle armor worn, in seconds
* Temperature from cooling food `temperatureFromCoolingFood`: Temperature change to apply to players after eating items with the tag `scorchful:is_cooling_food`

## Thirst Config

Server-side options for thirst and wetness.

* :star: Temperature from Wetness `temperatureFromWetness`: How much temperature to remove from wet entities, each tick
* Water from Refreshing food `waterFromRefreshingFood`: The amount of body water provided by consuming Refreshing food and drink
* Water from Sustaining food `waterFromSustainingFood`: The amount of body water provided by consuming Sustaining food and drink
* Water from Hydrating food `waterFromHydratingFood`: The amount of body water provided by consuming Hydrating food and drink
* Water from Parching food `waterFromParchingFood`: The amount of body water removed by consuming Parching food and drink
* Rehydration drink size `rehydrationDrinkSize`: The threshold for how much body water needs to be collected before Rehydration will automatically rehydrate the player
* Soaking from Splash Potions `soakingFromSplashPotions`: How much soaking is applied when an entity is hit with any Splash Potion
* Touching water or rain wetness increase per tick `touchingWaterWetnessIncrease`: How much to increase wetness by when touching water or rain each tick. Note that submerging yourself in water will fully soak you, regardless of what this is set to.
* On fire dry rate `onFireDryDate`: How many wetness points to remove each tick when on fire
* :star: Humid biome sweating efficiency `humidBiomeSweatEfficiency`: A multiplier for `temperatureFromWetness` when the humidity is in the range `[65%, 80%)`. This applies to rainy climates like Jungles, Swamps, etc.
* :star: Extra Humid biome sweating efficiency `extraHumidBiomeSweatEfficiency`: A multiplier for `temperatureFromWetness` when the humidity is at or above 80%. This applies to rainy climates like Jungles, Swamps during the Wet Season; to cave biomes; and to all non-arid biomes when it rains.
* :star: Arid biome sweating efficiency `aridBiomeSweatEfficiency`: A multiplier for `temperatureFromWetness` when the humidity is at or below 20%. This applies to arid climates like Deserts, Badlands, and Savannas, as well as The Nether.
* Maximum Rehydration Enchantment efficiency `maxRehydrationEfficiency`: Multiplier for `rehydrationDrinkSize` that controls the maximum amount of body water provided by a full suit of Rehydration armor

## Combat Config

Server-side options for combat features in Scorchful.

* :star: Fire Charge thrown type `fireBallThrownType`: One of SMALL, LARGE, or DISABLED. Controls what type of fireball is creating when throwing a fire charge. Small is just like firing from a Dispener and creates a fire on impact. Large is more like a Ghast and creates an explosion, and disabled disables this feature.
* :star: Enable Desert Visions `enableDesertVisions`: Enable/disable [Heat Visions](./Heat-Visions)
* Fear detection range multiplier `fearDetectionRangeMultiplier`: How much to multiply an entity's (including players) hostile mob detection range by.
* Impaling damage per level `impalingDamagePerLevel`: How much damage the Impaling enchantment should do to wet entities, per level of Impaling

## Weather Config

Server-side options for weather effects in Scorchful.

* :star: Do Sand Pile accumulation `doSandPileAccumulation`: Enable/disable sand piles accumulating on the ground during sandstorms
* Sand Pile accumulation height `sandPileAccumulationHeight`: Sets the maximum height of sand piles that can accumulate during sand storms. Maximum value is 8.
* Sandstorm Slowness amount (percent from -1 to +1) `sandstormSlownessAmountPercent`: How much to multiply the total speed of entities on the surface by during Sandstorms. Negative values provide slowness, positive values provide speed.
* Sandstorm Follow Range reduction amount (percent from -1 to +1) `sandstormFollowRangeReductionPercent`: How much to multiply the total follow range of entities on the surface by during Sandstorms. Negative values decrease follow range, positive values increase it.

## Mod Integration Config

Options for mod integrations in Scorchful.

### Dehydration Config

Options for integration with [Dehydration](https://modrinth.com/mod/dehydration).

* :star: Minimum water level required for sweating `minWaterLevelForSweat`: The minimum number of water points needed to be able to sweat (similar to how much hunger you need to heal)

## Removed Config Options

These reflect the removed config options from Scorchful, and their replacements (starting from Scorchful 0.15).

### Combat Config

* Default armor piece heat resistance `defaultArmorHeatResistance`: The default Heat Resistance to apply to all armor pieces that are not explicitly overridden
    * Replaced with the item component [`scorchful:heat_resistance`](./components.md#heat-resistance) 
* Very warm armor piece heat resistance `veryHarmfulArmorHeatResistance`: The amount of Heat Resistance to apply to very warm armor pieces (such as Fur Armor in Frostiful)
     * Replaced with the item component [`scorchful:heat_resistance`](./components.md#heat-resistance)
* Thermally protective armor piece heat resistance `protectiveArmorHeatResistance`: The amount of Heat Resistance to apply to protective armors, such as Netherite
    * Replaced with the item component [`scorchful:heat_resistance`](./components.md#heat-resistance)
* Very thermally protective armor piece heat resistance `veryProtectiveArmorHeatResistance`: The amount of Heat Resistance to apply to very protective armors, such as Turtle
    * Replaced with the item component [`scorchful:heat_resistance`](./components.md#heat-resistance) 
 
### Heating Config

* Minimum sky light level for heat `minSkyLightLevelForHeat`: The minimum *sky light* level that will start to apply environmental heating
    * Replaced with the [environment provider](https://thermoo.thedeathlycow.com/datapacks/environment_provider_definition/) `scorchful:modifier/apply_shade_for_time`.
* Heat from the Sun `heatFromSun`: How much environmental heating to apply from the Sun every tick
    * Replaced with the [environments](https://thermoo.thedeathlycow.com/datapacks/environment_definition/) `scorchful:temperature/scorching_climate`, `scorchful:temperature/warm_climate`, and `scorchful:temperature/temperate_climate`.
* Sun Hat shade temperature change `sunHatShadeTemperatureChange`: The temperature change to apply to players wearing a Sun Hat in Scorching Biomes each tick
    * Replaced with an [Environment Heat Resistance](https://thermoo.thedeathlycow.com/entity_attributes/) attribute modifier.
* Light level per heat increase (in The Nether) `lightLevelPerHeatInNether`: How many light levels correspond to a +1 increase in passive temperature, in The Nether
    * Replaced with the [environment](https://thermoo.thedeathlycow.com/datapacks/environment_definition/) `scorchful:hell`.
* Minimum light level for heat (in The Nether) `minLightLevelForHeatInNether`: The minimum light level to begin facing heat, in The Nether
    * Replaced with the [environment](https://thermoo.thedeathlycow.com/datapacks/environment_definition/) `scorchful:hell`.
* Blocks above Lava Ocean per heat increase (in The Nether) `blocksAboveLavaOceanPerHeatInNether`: How many blocks above the lava ocean correspond to a +1 increase in passive temperature, in The Nether
    * Replaced with the [environment](https://thermoo.thedeathlycow.com/datapacks/environment_definition/) `scorchful:hell`.
* Maximum heat from Lava Ocean (in The Nether) `maxHeatFromLavaOceanInNether`: The maximum passive heat that the Lava Ocean can create, in The Nether
    * Replaced with the [environment](https://thermoo.thedeathlycow.com/datapacks/environment_definition/) `scorchful:hell`.

### Thirst Config

* Dry rate `dryRate`: How many wetness points to remove each tick when not in contact with water
    * No replacement, hardcoded to be handled by Thermoo directly.

### Mod Integration Config
#### Seasons Config

* Enable seasons integration `enableSeasonsIntegration`: Toggles seasons integration. If set to false, Scorchful will treat all biomes as if it were Spring.
    * Replaced with the `thermoo:seasonal/temperate` and `thermoo:seasonal/tropical` [environment provider](https://thermoo.thedeathlycow.com/datapacks/environment_provider_definition/).
* Wet Season humid biome sweat efficiency `wetSeasonHumidBiomeSweatEfficiency`: How efficient [sweating](./Temperature-System#Sweating) is during the Wet Season in humid tropical biomes. This feature is exclusive to Serene Seasons.
    * Replaced with `thirstConfig/extraHumidBiomeSweatEfficiency` 
* Dry Season humid biome sweat efficiency `drySeasonHumidBiomeSweatEfficiency`: How efficient [sweating](./Temperature-System#Sweating) is during the Dry Season in humid tropical biomes. This feature is exclusive to Serene Seasons.
    * Replaced with `thirstConfig/aridBiomeSweatEfficiency` 