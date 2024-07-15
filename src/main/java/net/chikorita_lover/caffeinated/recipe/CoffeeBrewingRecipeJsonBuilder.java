package net.chikorita_lover.caffeinated.recipe;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CoffeeBrewingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Item output;
    private final Ingredient input;
    private final Ingredient reagent;
    private final float experience;
    private final int brewingTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    private final CoffeeBrewingRecipe.Serializer.RecipeFactory<?> recipeFactory;
    @Nullable
    private String group;

    private CoffeeBrewingRecipeJsonBuilder(RecipeCategory category, CoffeeBrewingRecipe.Serializer.RecipeFactory<?> recipeFactory, ItemConvertible output, Ingredient input, Ingredient reagent, float experience, int brewingTime) {
        this.category = category;
        this.recipeFactory = recipeFactory;
        this.output = output.asItem();
        this.input = input;
        this.reagent = reagent;
        this.experience = experience;
        this.brewingTime = brewingTime;
    }

    public static CoffeeBrewingRecipeJsonBuilder create(Ingredient input, Ingredient reagent, RecipeCategory category, ItemConvertible output, float experience, int cookingTime) {
        return new CoffeeBrewingRecipeJsonBuilder(category, CoffeeBrewingRecipe::new, output, input, reagent, experience, cookingTime);
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public CoffeeBrewingRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        this.validate(recipeId);
        Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(builder::criterion);
        CoffeeBrewingRecipe recipe = this.recipeFactory.create(Objects.requireNonNullElse(this.group, ""), this.input, this.reagent, new ItemStack(this.output), this.experience, this.brewingTime);
        exporter.accept(recipeId, recipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(Identifier recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}
