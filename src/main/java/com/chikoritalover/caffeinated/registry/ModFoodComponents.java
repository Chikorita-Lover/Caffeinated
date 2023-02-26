package com.chikoritalover.caffeinated.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent COFFEE_BERRIES = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.1F).build();
    public static final FoodComponent COFFEE_BOTTLE = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(ModStatusEffects.CAFFEINE, 3600, 0), 1.0F).build();
    public static final FoodComponent TIRAMISU = (new FoodComponent.Builder()).hunger(5).saturationModifier(0.6F).statusEffect(new StatusEffectInstance(ModStatusEffects.CAFFEINE, 600, 1), 1.0F).build();
}
