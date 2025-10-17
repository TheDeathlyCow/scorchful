# Scorchful

A Dune-inspired Minecraft mod focused on Heat-based survival and combat. Sister mod of [Frostiful](https://github.com/TheDeathlyCow/frostiful/)!

## Mod Pages

* Modrinth: https://modrinth.com/mod/scorchful
* CurseForge: https://www.curseforge.com/minecraft/mc-mods/scorchful

The above mod pages and this Git repository are the only official sources for this mod. Other sources may contain out of date or even maliciously modified versions of this mod. 

## Wiki and Documentation

Scorchful has a soon-to-be comprehensive player and developer wiki available at https://modded.wiki/w/Mod:Scorchful. Thanks to Patbox for hosting this!

If you have questions or problems, tech support is also provided on my [community Discord](https://discord.thedeathlycow.com). However, it is preferred that bug reports are submitted to the [issue tracker](https://github.com/TheDeathlyCow/scorchful/issues).

## Mod Integrations

This documents mod integrations that have been created specifically for Scorchful and are included with Scorchful out of the box.

Many other patches that are also relevant for Scorchful are provided by the standalone [Thermoo Patches](https://modrinth.com/mod/thermoo-patches) mod, including season integration, heart bar fixes, and more.

* [Enchantment Descriptions](https://modrinth.com/mod/enchantment-descriptions): Descriptions are provided for Scorchful's enchantments
* [Farmer's Delight](https://modrinth.com/mod/farmers-delight-refabricated): Foods and drinks provide water for sweating
* [Immersive Weathering](https://modrinth.com/mod/immersive-weathering): Icicles are cooling
* [Let's Do: Beach Party](https://modrinth.com/mod/lets-do-beachparty): Alcoholic drinks are parching, ice creams are cooling
* [Let's Do: Brewery](https://modrinth.com/mod/lets-do-brewery): Alcoholic drinks are parching
* [Let's Do: Herbal Brews](https://modrinth.com/mod/lets-do-herbalbrews): Teas provide water for sweating
* [Let's Do: Candlelight](https://modrinth.com/mod/lets-do-candlelight): Foods and drinks provide water for sweating
* [Let's Do: Vinery](https://modrinth.com/mod/lets-do-vinery): Alcoholic drinks are parching, grapes are refreshing
* [Let's Do: Meadow](https://modrinth.com/mod/lets-do-meadow): Milk is rehydrating

## Technical info

Scorchful is a mod for [Minecraft: Java Edition](https://www.minecraft.net/en-us/store/minecraft-deluxe-collection-pc), written using the [Fabric Mod Loader](https://fabricmc.net/), with support also provided for the [Quilt Mod Loader](https://quiltmc.org/en/). Scorchful depends on [Fabric API](https://github.com/FabricMC/fabric), [Thermoo](https://github.com/TheDeathlyCow/thermoo/), and [Cloth Config](https://github.com/shedaniel/cloth-config), and has integrations for its sister mod, [Frostiful](https://github.com/TheDeathlyCow/frostiful/). If using Quilt, then Fabric API should be replaced with [Quilt Standard Libraries](https://github.com/QuiltMC/quilt-standard-libraries).

## License 

Scorchful is licensed under LGPL-3.0. 

## Additional Credits

Thanks to everyone who has contributed to Scorchful, no matter how big or small! See the [Credits](./CREDITS.md) for the full list of contributors.

## Building 

Scorchful is built using [Gradle](https://gradle.org/) using the [Fabric Loom Gradle plugin](https://github.com/FabricMC/fabric-loom).

```bash
# builds a production jar of Frostiful
./gradlew build 

# runs Frostiful's unit tests
./gradlew check

# runs Frostiful's game tests
./gradlew runGametest
```

# LTS Policy

This is the current support status for each version of Minecraft that Scorchful is available for. The current Long-Term Support (LTS) policy for Scorchful versions is to support 1.21.1 and the latest game drop. Version 1.21.1 will be supported until at least the release of the Vibrant Visuals update for Java Edition.

Supported versions will receive all new features, fixes, and updates (where possible).

Unsupported versions version will receive no future updates, except for critical security fixes.

| Minecraft Version | Support Status |
|-------------------|----------------|
| 1.21.9            | ✅ Supported    | 
| 1.21.6-8          | ❌ Unsupported  | 
| 1.21.5            | ❌ Unsupported  | 
| 1.21.4            | ❌ Unsupported  | 
| 1.21.3            | ❌ Unsupported  | 
| 1.21.1            | ✅ Supported    | 
| 1.20.4            | ❌ Unsupported  | 
| 1.20.2            | ❌ Unsupported  | 
| 1.20.1            | ❌ Unsupported  |
| 1.19.4            | ❌ Unsupported  |
| 1.19.2            | ❌ Unsupported  | 
