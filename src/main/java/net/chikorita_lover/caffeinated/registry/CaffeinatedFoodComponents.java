package net.chikorita_lover.caffeinated.registry;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class CaffeinatedFoodComponents {
    public static final FoodComponent COFFEE_BERRIES = new FoodComponent.Builder().nutrition(2).saturationModifier(0.1F).build();
    public static final FoodComponent COFFEE_BOTTLE = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 3600, 1), 1.0F).build();
    public static final FoodComponent LATTE_COFFEE_BOTTLE = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 3200, 0), 1.0F).build();
    public static final FoodComponent CAFE_MIEL_COFFEE_BOTTLE = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 1800, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 240, 0), 1.0F).build();
    public static final FoodComponent TIRAMISU_SLICE = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 600, 1), 1.0F).snack().build();
}
