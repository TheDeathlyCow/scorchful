---
title: 🏠 Homepage
---
# Scorchful Config Wiki

Welcome to the Scorchful Config Wiki! This is a technical wiki specifically meant for addon and modpack developers who wish to configure Scorchful. For information about the content of Frostiful (blocks, items, etc) see: [https://github.com/TheDeathlyCow/scorchful/wiki](https://github.com/TheDeathlyCow/frostiful/wiki).

!!! warning
    Some links to content within Scorchful on this wiki are currently broken. These links will be fixed when Scorchful gets a new content wiki in the near future.

## 🌡️🐮 Thermoo Documentation

Scorchful uses [Thermoo](https://github.com/TheDeathlyCow/thermoo) for its internal temperature and environment system. Mod pack authors may find its documentation useful. You can find the Thermoo Developer Wiki here: [https://thermoo.thedeathlycow.com/](https://thermoo.thedeathlycow.com/). This wiki is written with the assumption of general understanding of Thermoo.

## 🧭 Navigation

- [Config File](./config.md)
- [Datapack Tags](./tags.md)
- [Component Types](./components.md)
- [Custom Temperature Effect Types](./temperature_effect_types.md)

## Compatibility

While this mod is designed to be as compatible as possible with other mods, it is inevitable that issues will arise. Specific incompatibilities will be documented here as they become known.

* [Hephaestus / Tinker's Construct](https://github.com/Alpha-s-Stuff/TinkersConstruct): Temperature bar not showing up. **FIX:** set `extraHeartRenderer` to `false` in the `mantle-client.toml` config.

### Other Temperature Mods

Most other temperature mods do not work with Scorchful's temperature system and likely never will. Running Scorchful with these mods is redundant as they also add their own temperature system in addition to Scorchful. These mods may technically run together, however if you want to have a cold temperature system with Scorchful I would recommend using [Frostiful](https://modrinth.com/mod/frostiful) instead, as Scorchful was made to work together with Frostiful.

All of the following temperature mods should not be used with Scorchful (note that this list is not exhaustive, these are just the ones I've heard of):
* PyroFrost
* EnvironmentZ
* Thermite
* Tough as Nails
* Cold Sweat
* Homeostatic
* Metabolism

For the mods that add thirst - [Dehydration](https://modrinth.com/mod/dehydration) is integrated with Scorchful and does not add a temperature system.
