package com.github.thedeathlycow.scorchful.entity;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DrownedEquipmentSelector {
    private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    );

    private static final WeightedList<DrownedArmorType> ARMOR_POOL = WeightedList.<DrownedArmorType>builder()
            .add(DrownedArmorType.COPPER, 3)
            .add(DrownedArmorType.TURTLE, 1)
            .build();

    public static void populateDrownedArmor(Drowned drowned, RandomSource random, DifficultyInstance difficulty) {
        if (!ScorchfulConfig.getEntityConfig().enableDrownedArmorSpawning()) {
            return;
        }

        if (random.nextFloat() < 0.25f * difficulty.getSpecialMultiplier()) {
            final DrownedArmorType armorType = ARMOR_POOL.getRandomOrThrow(random);
            final float partialChance = drowned.level().getDifficulty() == Difficulty.HARD ? 0.1f : 0.25f;
            boolean first = true;

            for (EquipmentSlot slot : ARMOR_SLOTS) {
                if (!first && random.nextFloat() < partialChance) {
                    break;
                }

                first = false;

                ItemStack itemStack = drowned.getItemBySlot(slot);

                if (itemStack.isEmpty()) {
                    Item equip = armorType.armor.getItemForSlot(slot);

                    if (equip != null) {
                        drowned.setItemSlot(slot, equip.getDefaultInstance());
                    }
                }
            }
        }
    }

    private record ArmorSet(
            Item helmet,
            Item chestplate,
            Item leggings,
            Item boots
    ) {
        @Nullable
        public Item getItemForSlot(EquipmentSlot slot) {
            return switch (slot) {
                case HEAD -> helmet;
                case CHEST -> chestplate;
                case LEGS -> leggings;
                case FEET -> boots;
                default -> null;
            };
        }
    }

    private enum DrownedArmorType {
        TURTLE(new DrownedEquipmentSelector.ArmorSet(Items.TURTLE_HELMET, SItems.TURTLE_CHESTPLATE, SItems.TURTLE_LEGGINGS, SItems.TURTLE_BOOTS)),
        COPPER(new DrownedEquipmentSelector.ArmorSet(Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS));

        private final ArmorSet armor;

        DrownedArmorType(ArmorSet armor) {
            this.armor = armor;
        }
    }

    private DrownedEquipmentSelector() {

    }
}