package com.github.thedeathlycow.scorchful.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class ScorchfulTurtleArmorMaterial implements ArmorMaterial {

    public static final ArmorMaterial INSTANCE = new ScorchfulTurtleArmorMaterial();

    @Override
    public int getDurability(ArmorItem.Type type) {
        return ArmorMaterials.TURTLE.getDurability(type);
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return ArmorMaterials.TURTLE.getProtection(type);
    }

    @Override
    public int getEnchantability() {
        return ArmorMaterials.TURTLE.getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.TURTLE.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ArmorMaterials.TURTLE.getRepairIngredient();
    }

    @Override
    public String getName() {
        return "scorchful/turtle";
    }

    @Override
    public float getToughness() {
        return ArmorMaterials.TURTLE.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.TURTLE.getKnockbackResistance();
    }

    private ScorchfulTurtleArmorMaterial() {

    }
}
