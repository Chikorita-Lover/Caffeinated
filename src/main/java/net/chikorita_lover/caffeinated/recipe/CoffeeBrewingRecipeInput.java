package net.chikorita_lover.caffeinated.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record CoffeeBrewingRecipeInput(ItemStack input, ItemStack reagent) implements RecipeInput {
    public ItemStack getStackInSlot(int slot) {
        ItemStack stack;
        switch (slot) {
            case 0 -> stack = this.input;
            case 1 -> stack = this.reagent;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        }
        return stack;
    }

    public int getSize() {
        return 2;
    }

    public boolean isEmpty() {
        return this.input.isEmpty() && this.reagent.isEmpty();
    }
}
