package net.chikorita_lover.caffeinated.registry;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class CaffeinatedFoodComponents {
    public static final FoodComponent COFFEE_BERRIES = new FoodComponent.Builder().nutrition(2).saturationModifier(0.1F).build();
    public static final FoodComponent COFFEE_BOTTLE = createCoffeeBottle(3600).build();
    public static final FoodComponent MILK_COFFEE_BOTTLE = createCoffeeBottle(3200).build();
    public static final FoodComponent HONEY_COFFEE_BOTTLE = createCoffeeBottle(1800).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 240, 0), 1.0F).build();
    public static final FoodComponent TIRAMISU_SLICE = new FoodComponent.Builder().nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 600, 1), 1.0F).snack().build();

    private static FoodComponent.Builder createCoffeeBottle(int duration) {
        return new FoodComponent.Builder().nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, duration, 0), 1.0F);
    }
}
