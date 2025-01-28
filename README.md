# Scorchful

A Dune-inspired Minecraft mod focused on Heat-based survival and combat. Sister mod of [Frostiful](https://github.com/TheDeathlyCow/frostiful/)!

## Mod Pages

* Modrinth (preferred): https://modrinth.com/mod/scorchful
* CurseForge: https://www.curseforge.com/minecraft/mc-mods/scorchful

The above mod pages and this Git repository are the only official sources for this mod. Other sources may contain out of date or even maliciously modified versions of this mod. 

## Wiki

The wiki is available at https://github.com/TheDeathlyCow/scorchful/wiki. However, this wiki is still being constructed. In the meantime, any questions about the content or mechanics of Scorchful should be directed to the `#help-scorchful` channel of my Discord: https://discord.gg/aqASuWebRU

# Mod Integrations

This documents mod integrations that have been created specifically for Scorchful and are included with Scorchful out of the box.

Many other patches that are also relevant for Frostiful are provided by the standalone [Thermoo Patches](https://modrinth.com/mod/thermoo-patches) mod, including season integration, heart bar fixes, and more.

* [Tips](https://modrinth.com/mod/tips): Added some Frostiful-specific tips
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

Scorchful is a mod for [Minecraft: Java Edition](https://www.minecraft.net/en-us/store/minecraft-deluxe-collection-pc), written using the [Fabric Mod Loader](https://fabricmc.net/), with support also provided for the [Quilt Mod Loader](https://quiltmc.org/en/). Scorchful currently only supports Minecraft 1.20.4. Scorchful depends on [Fabric API](https://github.com/FabricMC/fabric), [Thermoo](https://github.com/TheDeathlyCow/thermoo/), and [Cloth Config](https://github.com/shedaniel/cloth-config), and has integrations for its sister mod, [Frostiful](https://github.com/TheDeathlyCow/frostiful/). If using Quilt, then Fabric API should be replaced with [Quilt Standard Libraries](https://github.com/QuiltMC/quilt-standard-libraries).

## License 

Scorchful is licensed under LGPLv3. 

## Additional Credits

Thanks to everyone who has contributed to Scorchful, no matter how big or small! See the [Credits](./CREDITS.md) for the full list of contributors.

## Building 

Scorchful is built using [Gradle](https://gradle.org/). You can use the following command to build the latest version of the mod:

```
./gradlew build
```

# LTS Policy

These are the current versions being supported by Scorchful.

| Minecraft Version | Support Status         |
|-------------------|------------------------|
| 1.21.1            | ✅ Supported            | 
| 1.20.4            | ❌ Unsupported          | 
| 1.20.2            | ❌ Unsupported          | 
| 1.20.1            | ⚠️ Critical fixes only |
| 1.19.4            | ❌ Unsupported          |
| 1.19.2            | ❌ Unsupported          | 

Status Definitions:

* ✅ Supported: This version is fully supported and will receive all new features, fixes, and updates (where possible)
* ⚠️ Critical fixes only: This version will receive only critical crash and security fixes, as well as minor features where they can be easily cherry-picked
* ❌ Unsupported: This version will receive no future updates, except for critical security fixes