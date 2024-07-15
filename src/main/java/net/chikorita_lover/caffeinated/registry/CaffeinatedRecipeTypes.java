package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.recipe.CoffeeBrewingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CaffeinatedRecipeTypes {
    public static final RecipeType<CoffeeBrewingRecipe> COFFEE_BREWING = Registry.register(Registries.RECIPE_TYPE, Caffeinated.of("coffee_brewing"), new RecipeType<CoffeeBrewingRecipe>() {
        public String toString() {
            return "coffee_brewing";
        }
    });

    public static void register() {
    }
}
