---
title: 📋 Datapack Tags
---

This page describes the various datapack tags used by Scorchful. It includes both new tags added by Scorchful, and tags from Thermoo that Scorchful appends to.

!!! warning
    This page reflects the latest set of tags available for Minecraft 1.21.1. Items documented here may not necessarily reflect what appears in an older version of Scorchful. Starting from Scorchful 0.15, removed options will be documented in [Removed Tags](#removed-tags).

!!! info
    Items marked with [DG] are data generated, and so their definitions are found in `src/main/generated/data/`. All other tags are defined in `src/main/resources/data/`.

## Armor Material Tags

Location: `data/(scorchful|thermoo)/tags/armor_material`.

| Tag ID                           | Description                                                          | Default values (summarized)         |
|----------------------------------|----------------------------------------------------------------------|-------------------------------------|
| `scorchful:heat_neutral`         | All armor materials that provide no heat resistance buffs or debuffs | Chainmail and gold                  |
| `thermoo:resistant_to_heat`      | All armor materials that provide heat resistance buffs               | Netherite and Armadillo             |
| `thermoo:very_resistant_to_heat` | All armor materials that provide *strong* heat resistance buffs      | Vanilla turtle and Scorchful turtle |
| `thermoo:weak_to_heat`           | All armor materials that provide heat resistance debuffs             | Iron, Diamond, Leather              |

See [the Config file page](./config#combat-config) for how to customize the heat resistance values.

These tags will apply a [`scorchful:frost_resistance`](./components.md#heat-resistance) component to any new stacks created with these tags that does not already have this component. Editing these tags and reloading will not update existing stacks.

!!! warning
    These tags are deprecated, it is preferred that you use a the [`scorchful:frost_resistance`](./components.md#heat-resistance) component to modify attributes of custom armours instead.


## Block Tags

Location: `data/scorchful/tags/block`.

| Tag ID                                   | Description                                                                                            | Default values (summarized)                                            |
|------------------------------------------|--------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| `scorchful:heavy_ice`                    | What ice blocks can provide a 'cool floor' effect                                                      | All ice blocks                                                         |
| `scorchful:nether_lily_can_absorb_water` | What blocks that [Nether Lillies](./Nether-Lillies) can absorb water from when placed on in the Nether | Rooted Nylium, Rooted Netherrack, Rooted Dirt                          |
| `scorchful:nether_root_replaceable`      | What blocks Rooted Nylium can replaced when generating a Nether Lily patch                             | Netherrack, Nylium, Rooted Nylium                                      |
| `scorchful:sand_cauldrons`               | The [sand cauldrons](./Sand-Cauldrons)                                                                 | Sand Cauldron, Red Sand Cauldron                                       |
| `scorchful:sand_pile_can_survive_on`     | What blocks [sand piles](./Sand-Pile) can be placed on                                                 | Honey Blocks, Soul Sand, Mud, other blocks that Snow Layers survive on |
| `scorchful:sand_pile_cannot_survive_on`  | What blocks [sand piles](./Sand-Pile) can NOT be placed on                                             | Barrier blocks                                                         |
| `scorchful:sand_piles`                   | The sand pile blocks                                                                                   | Sand Pile, Red Sand Pile                                               |

## Entity Type Tags

Location: `data/(scorchful|thermoo)/tags/entity_type`.

| Tag ID                                 | Description                                                             | Default values (summarized)                                                              |
|----------------------------------------|-------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| `scorchful:crimson_lily_hurts`         | What entities that Crimson Lillies will hurt when stepped on            | Endermen, as well as enties extra suseptible to cold and entities that benefit from heat |
| `scorchful:does_not_slow_in_sandstorm` | Entities that are not slowed by the wind of a [Sandstorm](./Sandstorms) | Camels and Bosses                                                                        |
| `thermoo:benefits_from_heat`           | Entities that benefit from being warm                                   | Striders, Blazes, Magma Cubes, as well as enties extra suseptible to cold                |
| `thermoo:heat_immune`                  | Entities that are fully immune to heat                                  | Husks, Camels, Wither Skeletons, Ghasts, and Bosses                                      |
| `scorchful:unbendable_mind`            | A base tag for entities that cannot be affected by illusion magic       | Ender Dragons, Withers, Wardens, Elder Guardians, Phantoms                               |
| `scorchful:immune_to_fear`             | Entities that are immune to [Fear](./Fear)                              | `#scorchful:unbendable_mind`                                                             |

## Item Tags

Location: `data/scorchful/tags/item`.

| Tag ID                               | Description                                                                                        | Default values (summarized)                                                                            |
|--------------------------------------|----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| Water based tags                     | Determines how much water to give (or take) from the player when they consume certain food/drink   | See the [temperature system page](./Temperature-System#Sweating) for a better explanation and summary. |
| `scorchful:is_sun_protecting_hat`    | Items that, when worn in the Head slot, provide a shade effect similar to the [Sun Hat](./Sun-Hat) | Sun Hat                                                                                                |
| `scorchful:sand_piles`               | The sand pile items                                                                                | Sand Pile, Red Sand Pile                                                                               |
| `scorchful:turtle_armor`             | The turtle armor set                                                                               | All Turtle Armour items, including the vanilla Turtle Helmet                                           |
| `scorchful:is_cooling_food`          | Directly applies cooling to players when consumed                                                  | Ice creams from Let's Do Beachparty, and Icicles from Immersive Weathering                             |
| `scorchful:blocks_rain_when_holding` | Blocks soaking effects from rain when held in main or off hand                                     | Umbrella items from Origins: Umbrellas and Artifacts                                                   |

## Biome Tags

Location: `data/scorchful/tags/worldgen/biome`

| Tag ID                                            | Description                                                       | Default values (summarized)                                      |
|---------------------------------------------------|-------------------------------------------------------------------|------------------------------------------------------------------|
| `scorchful:has_feature/crimson_lily_patch`        | Which biomes Crimson Lily patches generate in                     | Crimson Forest                                                   |
| `scorchful:has_feature/sparse_crimson_lily_patch` | Which biomes Sparse Crimson Lily patches generate in              | Nether Wastes                                                    |
| `scorchful:has_structure/warped_lily_farm`        | Which biomes Warped Lily Farms generate in                        | Warped Forest                                                    |
| 'Heat vision' tags                                | Controls the biomes of the various [Heat Visions](./Heat-Visions) | See [Heat Visions](./Heat-Visions) for a more detailed breakdown |
| `scorchful:has_red_sand_storms`                   | Which biomes should have Red Sandstorms                           | All badlands-like biomes                                         |
| `scorchful:has_regular_sand_storms`               | Which biomes should have regular Sandstorms                       | All Desert-like biomes                                           |
| `scorchful:is_climate/*` [DG]                     | Set the biomes for each climate                                   | See [temperature system page](./Temperature-System)              |
| `scorchful:is_not_climate/*` [DG]                 | Excludes biomes from a particular climate                         | See [temperature system page](./Temperature-System)              |
| `scorchful:is_extreme_humidity` [DG]              | Biomes that have extremely high or extremely low humidity         | Rainy Climates, Arid Climates, and Caves                         |
| `scorchful:is_never_warm` [DG]                    | Biomes that are never warm                                        | Empty                                                            |

## Environment Providers

Location: `data/scorchful/tags/thermoo/environment_provider`.

[Environment Provider](https://thermoo.thedeathlycow.com/datapacks/environment_provider_definition/) is a custom registry, defined in Thermoo.

| Tag ID                                 | Description                                               | Default values (summarized)                                |
|----------------------------------------|-----------------------------------------------------------|------------------------------------------------------------|
| `scorchful:humidity_modifiers` [DG]    | Set humidity for overworld biomes                         | Set climate humidity, set humidity for weather             |
| `scorchful:nether_modifiers` [DG]      | Sets humidity and adjust temperature for nether biomes    | Set nether humidity, add temperature for block light       |
| `scorchful:temperature_modifiers` [DG] | Sets humidity and adjust temperature for overworld biomes | `#scorchful:humidity_modifiers`, set temperature for shade |

## Removed Tags

### Biome Tags

| Tag ID                                | Description                                                                              | Default values (summarized)                                                         | Replacement                                                      |
|---------------------------------------|------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|------------------------------------------------------------------|
| Seasonal Temperature tags             | Which tags are used to define warm and scorching biomes, depending on the current season | See [Overworld Temperature System page](./Temperature-System#Overworld) for details | `#scorchful:is_climate/*` and `#scorchful:is_not_climate/*` tags |
| `scorchful:humid_biomes`              | Which biomes are 'humid' in the temperature system                                       | See [the Temperature System page](./Temperature-System#Cooling) for details         | `#scorchful:is_climate/rainy`                                    |
| `scorchful:temperature/is_never_warm` | Which biomes are never warm, regardless of season                                        | Stony Peaks and End biomes                                                          | `scorchful:temperature/is_never_warm`                            |
